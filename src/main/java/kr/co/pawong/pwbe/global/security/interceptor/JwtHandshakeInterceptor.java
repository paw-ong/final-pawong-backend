package kr.co.pawong.pwbe.global.security.interceptor;

import java.util.List;
import java.util.Map;
import kr.co.pawong.pwbe.global.security.util.JwtTokenProvider;
import kr.co.pawong.pwbe.user.application.port.in.QueryUserDataUseCase;
import kr.co.pawong.pwbe.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * WebSocket 핸드셰이크 단계 즉 클라이언트가 최초로 /ws (또는 SockJS 의 /ws/info) 엔드포인트로 HTTP 요청을 보낼 때 한번만 호출
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final QueryUserDataUseCase queryUserDataUseCase;

    /**
     * 클라이언트 → GET /ws?token=… (혹은 SockJS GET /ws/info?token=…)
     *
     * @param request    the current request
     * @param response   the current response
     * @param wsHandler  the target WebSocket handler
     * @param attributes the attributes from the HTTP handshake to associate with the WebSocket
     *                   session; the provided attributes are copied, the original map is not used.
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) {
        // 폴백 통과
        String path = request.getURI().getPath();
        if (path.endsWith("/info")
                || path.contains("/xhr_streaming")
                || path.contains("/eventsource")
                || path.contains("/htmlfile")
                || path.contains("/iframe.html")
        ) {
            return true;
        }

        // 폴백이 아닌 순수 GET /ws?token=… 이라도, 실제 websocket 업그레이드가 아닐 땐 통과
        List<String> upgrade = request.getHeaders().get("Upgrade");
        if (upgrade == null || !upgrade.contains("websocket")) {
            return true;
        }


        String token = UriComponentsBuilder.fromUri(request.getURI())
                .build()
                .getQueryParams()
                .getFirst("token");
        if (!StringUtils.hasText(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
        if (!jwtTokenProvider.validateAccessTokenOrThrow(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        String userId = jwtTokenProvider.getUsername(token);
        User user = queryUserDataUseCase.getUser(Long.valueOf(userId));
        UserDetails userDetails = jwtTokenProvider.getUserDetails(token,
                user.getSocialId());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        attributes.put("auth", authentication);
        return true;
    }

    /**
     * 핸드셰이크 성공 → WebSocket 업그레이드 완료 이후 호출
     *
     * @param request   the current request
     * @param response  the current response
     * @param wsHandler the target WebSocket handler
     * @param exception an exception raised during the handshake, or {@code null} if none
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Exception exception) {

    }
}

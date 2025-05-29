package kr.co.pawong.pwbe.global.security.interceptor;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.TOKEN_INVALIDATE;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import kr.co.pawong.pwbe.global.security.error.exception.FilterAuthenticationException;
import kr.co.pawong.pwbe.global.security.service.AuthContextService;
import kr.co.pawong.pwbe.global.security.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.WebUtils;

/**
 * WebSocket 핸드셰이크 단계 즉 클라이언트가 최초로 /ws (또는 SockJS 의 /ws/info) 엔드포인트로 HTTP 요청을 보낼 때 한번만 호출
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthContextService authContextService;

    /**
     * 클라이언트 → GET /ws?token=… (혹은 SockJS GET /ws/info?token=…)
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
        ) return true;


        // 폴백이 아닌 순수 GET /ws?token=… 이라도, 실제 websocket 업그레이드가 아닐 땐 통과
        List<String> upgrade = request.getHeaders().get("Upgrade");
        if (upgrade == null || !upgrade.contains("websocket"))
            return true;

        // 쿠키에서 토큰 가져오기
        String token = resolveToken(request);

        try {
            // CASE 1: 유효한 Access Token
            if (jwtTokenProvider.validateAccessTokenOrThrow(token)) {
                attributes.put("auth", authContextService.createAuthContext(token));
                return true;
            }
            response.setStatusCode(HttpStatus.UNAUTHORIZED);

        } catch (ExpiredJwtException | JwtException | IllegalArgumentException bad) {
            throw new FilterAuthenticationException(TOKEN_INVALIDATE);
        }
        return true;
    }

    /**
     * ServerHttpRequest → HttpServletRequest로 변환해서
     * "ACCESS_TOKEN" 쿠키를 찾아 그 값을 리턴하거나 null
     */
    private String resolveToken(ServerHttpRequest request) {
        if (!(request instanceof ServletServerHttpRequest)) {
            return null;
        }
        HttpServletRequest servletReq = ((ServletServerHttpRequest) request).getServletRequest();
        Cookie cookie = WebUtils.getCookie(servletReq, "ACCESS_TOKEN");
        return (cookie != null ? cookie.getValue() : null);
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

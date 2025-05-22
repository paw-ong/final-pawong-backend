package kr.co.pawong.pwbe.user.adapter.out.security.filter;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.TOKEN_INVALIDATE;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import kr.co.pawong.pwbe.user.domain.User;
import kr.co.pawong.pwbe.user.adapter.out.security.JwtTokenProvider;
import kr.co.pawong.pwbe.user.adapter.out.security.error.exception.FilterAuthenticationException;
import kr.co.pawong.pwbe.user.application.port.in.QueryUserDataUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final QueryUserDataUseCase queryUserDataUseCase;

    public JwtFilter(
            JwtTokenProvider jwtTokenProvider,
            QueryUserDataUseCase queryUserDataUseCase) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.queryUserDataUseCase = queryUserDataUseCase;
    }

    private boolean shouldSkip(String uri) {
        return uri.startsWith("/oauth2/authorization")
                || uri.startsWith("/oauth2/authorize")
                || uri.startsWith("/login/oauth2/code")  // oauth
                || uri.startsWith("/api/adoptions")
                || uri.startsWith("/api/adoption")
                || uri.startsWith("/api/shelters")
                || uri.startsWith("/api/lost-animals")
                || uri.startsWith("/ws")
                || uri.startsWith("/api/auth/csrf-token")
                ;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();

        // 제외 URL 검사
        if (shouldSkip(uri)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = extractToken(request);

        if (jwtTokenProvider.validateToken(token)) {
            String userId = jwtTokenProvider.getUsername(token);

            try {
                User user = queryUserDataUseCase.getUser(Long.valueOf(userId));
                // userId와 socialId로 customOauthUserDetail을 만든 Authentication 객체를 SecurityContext에 저장합니다.
                UserDetails userDetails = jwtTokenProvider.getUserDetails(token,
                        user.getSocialId());
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null,
                                userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (BaseException exception) {
                throw new FilterAuthenticationException(exception.getErrorCode());
            }
        } else {
            throw new FilterAuthenticationException(TOKEN_INVALIDATE);
        }
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}


package kr.co.pawong.pwbe.global.security.filter;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.ACCESS_TOKEN_EXPIRED;
import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.ACCESS_TOKEN_INVALIDATE;
import static kr.co.pawong.pwbe.global.security.util.JwtUtil.getCookieValue;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import kr.co.pawong.pwbe.global.security.error.exception.FilterAuthenticationException;
import kr.co.pawong.pwbe.global.security.service.AuthContextService;
import kr.co.pawong.pwbe.global.security.util.JwtTokenProvider;
import kr.co.pawong.pwbe.user.application.port.in.QueryUserDataUseCase;
import kr.co.pawong.pwbe.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthContextService authContextService;

    // 스킵할 URI(인증이 필요 없는 엔드포인트)
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        return uri.startsWith("/oauth2/authorization")
                || uri.startsWith("/oauth2/authorize")
                || uri.startsWith("/login/oauth2/code")
                || uri.startsWith("/api/adoptions")
                || uri.startsWith("/api/adoption")
                || uri.startsWith("/api/shelters")
                || (HttpMethod.GET.matches(method) && uri.startsWith("/api/lost-animals"))
                || uri.startsWith("/api/lost-animals")
                || uri.startsWith("/ws")
                || uri.startsWith("/api/auth/csrf-token")
                ;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String accessToken = getCookieValue(request, "ACCESS_TOKEN");
            jwtTokenProvider.validateAccessTokenOrThrow(accessToken);
            authContextService.createAuthContext(accessToken);
            filterChain.doFilter(request, response);
        } catch (JwtException | IllegalArgumentException e) {
            throw new FilterAuthenticationException(ACCESS_TOKEN_INVALIDATE);
        }
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}


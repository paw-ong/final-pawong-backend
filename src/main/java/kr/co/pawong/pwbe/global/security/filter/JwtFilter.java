package kr.co.pawong.pwbe.global.security.filter;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.ACCESS_TOKEN_INVALIDATE;
import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.REFRESH_TOKEN_INVALIDATE;
import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.TOKEN_INVALIDATE;
import static kr.co.pawong.pwbe.global.security.util.JwtUtil.getCookieValue;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import kr.co.pawong.pwbe.global.security.error.exception.FilterAuthenticationException;
import kr.co.pawong.pwbe.global.security.service.AuthContextService;
import kr.co.pawong.pwbe.global.security.service.RefreshTokenService;
import kr.co.pawong.pwbe.global.security.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseCookie;
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
    private final RefreshTokenService refreshTokenService;

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
        log.info("jwtFilter");


        /**
         * CASE 1: Valid Access Token
         */
        String accessToken = getCookieValue(request, "ACCESS_TOKEN");

        try {
            // CASE 1: 유효한 Access Token
            if (jwtTokenProvider.validateAccessTokenOrThrow(accessToken)) {
                authContextService.createAuthContext(accessToken);
                log.info("valid Access");
                filterChain.doFilter(request, response);
                return;
            }
        } catch (ExpiredJwtException expired) {
            // 토큰 만료 → CASE 2/3 로 내려가서 Refresh 로직 수행
        } catch (JwtException | IllegalArgumentException bad) {
            // 서명 오류 / 포맷 오류
            throw new FilterAuthenticationException(ACCESS_TOKEN_INVALIDATE);
        }

        /**
         * CASE 2: Invalid Access Token + Invalid Refresh Token
         */
        String refreshToken = getCookieValue(request, "REFRESH_TOKEN");
        Claims refreshClaims;
        try {
            refreshClaims = jwtTokenProvider.parseAndValidateRefreshToken(refreshToken);
            if (refreshClaims == null) throw new JwtException("Expired");
        } catch (JwtException e) {
            throw new FilterAuthenticationException(REFRESH_TOKEN_INVALIDATE);
        }
        // Redis에 저장된 토큰과 일치하는지 검증 -> 일치하지 않으면 401
        Long userId = Long.valueOf(jwtTokenProvider.getRefreshUsername(refreshToken));
        if (!refreshTokenService.isValidRefresh(userId, refreshToken)) {
            throw new FilterAuthenticationException(TOKEN_INVALIDATE);
        }

        /**
         * CASE 3: Valid Refresh Token → 새 토큰 발급
         */
        log.info("valid refreshToken");
        // 1) 새 토큰 생성
        String newAccess = jwtTokenProvider.createAccessToken(userId);
        String newRefresh = jwtTokenProvider.createRefreshToken(userId);

        // 2) Redis 교체
        refreshTokenService.rotateRefresh(userId, newRefresh);
        log.info("rotate");

        // 3) 새로운 쿠키 세팅
        ResponseCookie accessCookie = jwtTokenProvider.generateAccessTokenCookie(userId);
        ResponseCookie refreshCookie = jwtTokenProvider.generateRefreshTokenCookie(userId);

        response.setHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        // 4) 인증 객체 세팅
        authContextService.createAuthContext(newAccess);
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


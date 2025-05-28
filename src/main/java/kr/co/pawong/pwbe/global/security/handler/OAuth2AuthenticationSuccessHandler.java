package kr.co.pawong.pwbe.global.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import kr.co.pawong.pwbe.global.security.util.JwtTokenProvider;
import kr.co.pawong.pwbe.user.application.port.in.QueryUserDataUseCase;
import kr.co.pawong.pwbe.user.domain.User;
import kr.co.pawong.pwbe.user.enums.UserStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${spring.security.base-url}")
    private String baseUrl;
    private final QueryUserDataUseCase queryUserDataUseCase;
    private final JwtTokenProvider jwtTokenProvider;

    public OAuth2AuthenticationSuccessHandler(QueryUserDataUseCase queryUserDataUseCase,
            JwtTokenProvider jwtTokenProvider) {
        this.queryUserDataUseCase = queryUserDataUseCase;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String socialId = oauthUser.getName();
        User user = queryUserDataUseCase.getUserBySocialId(Long.valueOf(socialId));
        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        ResponseCookie accessTokenCookie = jwtTokenProvider.generateAccessTokenCookieByOauth2(authentication, user.getUserId());
        ResponseCookie refreshTokenCookie = jwtTokenProvider.generateRefreshTokenCookieByOauth2(authentication, user.getUserId());
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        // INACTIVE : 추가 정보 입력 페이지로 redirect
        if(user.getStatus() != UserStatus.ACTIVE) {
            response.sendRedirect(baseUrl + "/oauth2/redirect?status=PENDING");
            return;
        }
        // ACTIVE
        response.sendRedirect(baseUrl+"/oauth2/redirect?status=ACTIVE");
    }
}

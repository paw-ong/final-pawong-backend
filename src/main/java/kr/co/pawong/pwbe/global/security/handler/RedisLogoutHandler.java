package kr.co.pawong.pwbe.global.security.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.pawong.pwbe.global.security.service.RefreshTokenService;
import kr.co.pawong.pwbe.global.security.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
@RequiredArgsConstructor
public class RedisLogoutHandler implements LogoutHandler {
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        Cookie cookie = WebUtils.getCookie(request, "REFRESH_TOKEN");
        if (cookie == null) return;

        String refreshToken = cookie.getValue();
        Long userId = Long.valueOf(jwtTokenProvider.getRefreshUsername(refreshToken));
        refreshTokenService.deleteRefresh(userId);
    }
}

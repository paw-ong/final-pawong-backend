package kr.co.pawong.pwbe.global.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CookieClearingLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        // ACCESS_TOKEN 만료
        ResponseCookie expiredAccess = ResponseCookie.from("ACCESS_TOKEN", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        // REFRESH_TOKEN 만료
        ResponseCookie expiredRefresh = ResponseCookie.from("REFRESH_TOKEN", "")
                .httpOnly(true)
                .secure(false)
                .path("/")  // 토큰 발급할 때 쓰던 path 와 동일하게
                .maxAge(0)
                .sameSite("Lax")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, expiredAccess.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, expiredRefresh.toString());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}

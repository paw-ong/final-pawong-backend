package kr.co.pawong.pwbe.global.security.service;

import kr.co.pawong.pwbe.global.security.util.JwtTokenProvider;
import kr.co.pawong.pwbe.user.application.port.in.QueryUserDataUseCase;
import kr.co.pawong.pwbe.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthContextService {

    private final QueryUserDataUseCase queryUserDataUseCase;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * accessToken에서 userId를 추출하고,
     * userId와 socialId로 사용자 인증 객체를 생성하여
     * SecurityContextHolder에 추가합니다.
     * @param accessToken
     */
    public Authentication createAuthContext(String accessToken) {
        String userId = jwtTokenProvider.getAccessUsername(accessToken);
        User user = queryUserDataUseCase.getUser(Long.valueOf(userId));
        // userId와 socialId로 customOauthUserDetail을 만든 Authentication 객체를 SecurityContext에 저장합니다.
        UserDetails userDetails = jwtTokenProvider.getUserDetails(accessToken, user.getSocialId());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

}

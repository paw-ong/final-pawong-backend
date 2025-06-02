package kr.co.pawong.pwbe.global.security.service;


import java.util.List;
import kr.co.pawong.pwbe.global.security.dto.CustomUserDetails;
import kr.co.pawong.pwbe.user.application.port.in.QueryUserDataUseCase;
import kr.co.pawong.pwbe.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final QueryUserDataUseCase queryUserDataUseCase;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = queryUserDataUseCase.getUserBySocialId(Long.valueOf(username));
        return new CustomUserDetails(user.getUserId(), user.getSocialId(), List.of());
    }

}

package kr.co.pawong.pwbe.user.adapter.out.security;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.USERNAME_NOT_FOUND;

import kr.co.pawong.pwbe.user.adapter.out.jpa.entity.UserEntity;
import kr.co.pawong.pwbe.user.adapter.out.jpa.repository.UserJpaRepository;
import kr.co.pawong.pwbe.user.adapter.out.security.error.exception.FilterAuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;


    public CustomUserDetailsService(
            UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userJpaRepository.findBySocialId(Long.valueOf(username))
                .orElseThrow(() ->
                        new FilterAuthenticationException(USERNAME_NOT_FOUND,
                                "User not found with username: " + username));
        return new CustomUserDetails(userEntity);
    }

}

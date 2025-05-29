package kr.co.pawong.pwbe.global.security.dto;

import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Long userId;
    private final String username;  // 추후 email로 변경
    private final String password;
    private final List<? extends GrantedAuthority> authorities;

    // TODO: Long socialId를 String username으로 변경 (username에는 email을 저장)
    public CustomUserDetails(Long userId, Long socialId, List<SimpleGrantedAuthority> list) {
        this.userId = userId;
        this.username = String.valueOf(socialId);
        this.password = "";
        this.authorities = list;
    }

}

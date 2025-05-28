package kr.co.pawong.pwbe.global.security.dto;

import java.util.Collection;
import java.util.List;
import kr.co.pawong.pwbe.user.adapter.out.jpa.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

    private final Long userId;
    private final Long socialId;
    private final String username;
    private final String password;
    private final List<? extends GrantedAuthority> authorities;


    public CustomUserDetails(UserEntity userEntity) {
        this.userId = userEntity.getUserId();
        this.socialId = userEntity.getSocialId();
        this.username = String.valueOf(this.socialId);
        this.password = "";
        this.authorities = List.of();
    }

    public CustomUserDetails(Long userId, Long socialId, List<SimpleGrantedAuthority> list) {
        this.userId = userId;
        this.socialId = socialId;
        this.username = String.valueOf(socialId);
        this.password = "";
        this.authorities = list;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getSocialId() {
        return socialId;
    }


}

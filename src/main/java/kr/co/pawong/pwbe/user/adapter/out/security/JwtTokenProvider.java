package kr.co.pawong.pwbe.user.adapter.out.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    private final Key secretKey;
    @Getter
    private static final long tokenValidityInMs = 60L * 60 * 1000 * 1000;

    public JwtTokenProvider(@Value("${spring.security.jwt.secret-key}") String key) {
        this.secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }

    public String generateTokenByOauth2(Authentication authentication, Long userId) {
        DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
        return getToken(
            userId,
            oauth2User.getName(),       // socialId
            oauth2User.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())
        );
    }

    public String generateToken(Authentication authentication, Long userId) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return getToken(userId, userDetails.getUsername(), roles);
    }

    private String getToken(Long userId, String username, List<String> roles) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityInMs);
        return Jwts.builder()
                .setSubject(String.valueOf(userId))  // 사용자 식별 정보 (주로 username)
                .claim("roles", roles)
                .claim("username", username)          // 필요시 추가 정보// 권한 정보
                .setIssuedAt(now)                       // 발행 시간
                .setExpiration(validity)                // 만료 시간
                .signWith(this.secretKey)// 서명 알고리즘
                .compact();
    }

    public UserDetails getUserDetails(String token, Long socialId) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Long userId = Long.valueOf(claims.getSubject());
        List<String> roles = claims.get("roles", List.class);

        return new CustomUserDetails(
            userId,
            socialId,
            roles.stream().map(SimpleGrantedAuthority::new).toList()
        );
    }

    // 토큰이 유효한지 검증
    public boolean validateToken(String token) {
        if (token == null || getUsername(token) == null) {
            return false;
        }
        try {
            Claims claims = getClaims(token);
            return !isExpired(claims);
        } catch (JwtException | IllegalArgumentException e) {
            return false;   // 파싱 오류나 서명 오류, 만료 오류 등 모두 false 처리
        }
    }

    // 토큰의 만료 여부를 확인.
    // @return 만료되었으면 true, 아직 유효하면 false
    private boolean isExpired(Claims claims) {
        return claims.getExpiration().before(new Date());   // 만료 시간이 현재보다 뒤(미래)에 있어야 true
    }

    // token에서 userId를 꺼내오는 메소드
    // subject를 userId로 세팅해서
    public String getUsername(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

     // 토큰에서 Claims를 꺼내오기
     // 파싱 실패 시 예외 발생
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}

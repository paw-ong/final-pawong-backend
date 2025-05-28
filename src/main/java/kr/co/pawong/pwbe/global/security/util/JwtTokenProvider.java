package kr.co.pawong.pwbe.global.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import kr.co.pawong.pwbe.global.security.dto.CustomUserDetails;
import kr.co.pawong.pwbe.global.security.service.RefreshTokenService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

    @Getter
    private static final long tokenValidityInMs = 60L * 60 * 1000 * 1000;
    private final Key accessSecretKey;
    private final Key refreshSecretKey;
    @Value("${spring.security.jwt.access-token-validity-in-ms}")
    private long accessTokenValidityInMs;            // 15 minutes
    @Value("${spring.security.jwt.refresh-token-validity-in-ms}")
    private long refreshTokenValidityInMs;           // 7 days
    private final RefreshTokenService refreshTokenService;

    public JwtTokenProvider(
            @Value("${spring.security.jwt.access-secret-key}") String accessKey,
            @Value("${spring.security.jwt.refresh-secret-key}") String refreshKey,
            RefreshTokenService refreshTokenService
    ) {
        this.accessSecretKey = Keys.hmacShaKeyFor(accessKey.getBytes(StandardCharsets.UTF_8));
        this.refreshSecretKey = Keys.hmacShaKeyFor(refreshKey.getBytes(StandardCharsets.UTF_8));
        this.refreshTokenService = refreshTokenService;
    }

    public ResponseCookie generateAccessTokenCookieByOauth2(
            Authentication authentication,
            Long userId) {
        DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
        long maxAgeInSeconds = accessTokenValidityInMs / 1_000;
        return ResponseCookie.from("ACCESS_TOKEN", createAccessToken(
                        userId,
                        oauth2User.getName(),
                        oauth2User.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())
                ))
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(maxAgeInSeconds)
                .sameSite("Lax")
                .build();
    }
    public ResponseCookie generateRefreshTokenCookieByOauth2(Authentication authentication,
            Long userId) {
        DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
        long maxAgeInSeconds = refreshTokenValidityInMs / 1_000;
        String refresh = createAccessToken(
                userId,
                oauth2User.getName(),
                oauth2User.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())
        );
        refreshTokenService.store(userId, refresh);
        return ResponseCookie.from("REFRESH_TOKEN", refresh)
                .httpOnly(true)
                .secure(false)
                .path("/")              // 재발급 엔드포인트에서만 전송
                .maxAge(maxAgeInSeconds)
                .sameSite("Lax")
                .build();
    }

    public String generateToken(Authentication authentication, Long userId) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return createAccessToken(userId, userDetails.getUsername(), roles);
    }

    private String createAccessToken(Long userId, String username, List<String> roles) {
        // username = socialId (추후 email로 변경)
        Date now = new Date();
        Date exp = new Date(now.getTime() + accessTokenValidityInMs);
        return Jwts.builder()
                .setSubject(String.valueOf(userId))                 // 사용자 식별 정보 (주로 username)
                .claim("roles", roles)                        // 권한 정보
                .claim("username", username)                  // 필요시 추가 정보
                .setIssuedAt(now)                                   // 발행 시간
                .setExpiration(exp)                                 // 만료 시간
                .signWith(this.accessSecretKey, SignatureAlgorithm.HS256) // 서명 알고리즘
                .compact();
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
                .signWith(this.accessSecretKey)// 서명 알고리즘
                .setSubject(String.valueOf(userId))                 // 사용자 식별 정보 (주로 username)
                .claim("roles", roles)                        // 권한 정보
                .claim("username", username)                  // 필요시 추가 정보
                .setIssuedAt(now)                                   // 발행 시간
                .setExpiration(validity)                                 // 만료 시간
                .signWith(this.refreshSecretKey, SignatureAlgorithm.HS256) // 서명 알고리즘
                .compact();
    }

    public UserDetails getUserDetails(String token, Long socialId) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(accessSecretKey)
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
    public boolean validateAccessTokenOrThrow(String token) {
        if (token == null || getUsername(token) == null) {
            return false;
        }
        try {
            Claims claims = getClaims(token);
            return !isExpired(claims);
            // 만료 여부는 claims.getExpiration() 으로 직접 체크
        } catch (ExpiredJwtException e) {
            throw e;
            // 토큰 만료
        } catch (JwtException e) {
            // 서명 오류 / 토큰 변조 / 포맷 오류 등
            throw e;
        }
    }
    public Claims parseAndvalidateRefreshToken(String token) {
        if (token == null) {
            return null;
        }
        try {
            Claims refreshClaims = getRefreshClaims(token);
            if(!isExpired(refreshClaims))
                return refreshClaims;
            // 만료 여부는 claims.getExpiration() 으로 직접 체크
        } catch (ExpiredJwtException e) {
            throw e;
            // 토큰 만료
        } catch (JwtException e) {
            // 서명 오류 / 토큰 변조 / 포맷 오류 등
            throw e;
        }
        return null;
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
                .setSigningKey(accessSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getRefreshUsername(String token) {
        Claims claims = getRefreshClaims(token);
        return claims.getSubject();
    }

    private Claims getRefreshClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(refreshSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}

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
import kr.co.pawong.pwbe.global.security.dto.CustomUserDetails;
import kr.co.pawong.pwbe.global.security.service.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${spring.security.jwt.access-token-validity-in-ms}")
    private long accessTokenValidityInMs;            // 15 minutes
    private final Key accessSecretKey;

    @Value("${spring.security.jwt.refresh-token-validity-in-ms}")
    private long refreshTokenValidityInMs;           // 7 days
    private final Key refreshSecretKey;
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

    public ResponseCookie generateAccessTokenCookie(Long userId) {
        long maxAgeInSeconds = accessTokenValidityInMs / 1_000;
        String access = createAccessToken(userId);
        return ResponseCookie.from("ACCESS_TOKEN", access)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(maxAgeInSeconds)
                .sameSite("Lax")
                .build();
    }
    public ResponseCookie generateRefreshTokenCookie(Long userId) {
        long maxAgeInSeconds = refreshTokenValidityInMs / 1_000;
        String refresh = createRefreshToken(userId);
        refreshTokenService.storeRefresh(userId, refresh);
        return ResponseCookie.from("REFRESH_TOKEN", refresh)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(maxAgeInSeconds)
                .sameSite("Lax")
                .build();
    }

    // subject를 userId로 세팅
    public String createAccessToken(Long userId) {
        // 현재는 username = String.valueOf(userId) (추후 email로 변경)
        Date now = new Date();
        Date exp = new Date(now.getTime() + accessTokenValidityInMs);
        return Jwts.builder()
                .setSubject(String.valueOf(userId))                 // 사용자 식별 정보 (주로 username)
                .setIssuedAt(now)                                   // 발행 시간
                .setExpiration(exp)                                 // 만료 시간
                .signWith(this.accessSecretKey, SignatureAlgorithm.HS256) // 서명 알고리즘
                .compact();
    }
    public String createRefreshToken(Long userId) {
        // username = socialId (추후 email로 변경)
        Date now = new Date();
        Date exp = new Date(now.getTime() + refreshTokenValidityInMs);
        return Jwts.builder()
                .setSubject(String.valueOf(userId))                 // 사용자 식별 정보 (주로 username)
                .setIssuedAt(now)                                   // 발행 시간
                .setExpiration(exp)                                 // 만료 시간
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
//        List roles = claims.get("roles", List.class);
        return new CustomUserDetails(
                userId,
                socialId,
                List.of()
        );
    }

    // 토큰이 유효한지 검증
    public boolean validateAccessTokenOrThrow(String token) {
        if (token == null || getAccessUsername(token) == null) {
            return false;
        }

        try {
            Claims claims = getAccessClaims(token);
            if(!isExpired(claims)) return true;
        } catch (ExpiredJwtException expiredJwtException) {
            throw expiredJwtException;
        } catch (JwtException | IllegalArgumentException e) {
            // any other parsing/signature/format error
            throw new JwtException("Invalid access token", e);
        }

        return true;
    }
    public Claims parseAndValidateRefreshToken(String token) {
        if (token == null) {
            return null;
        }
        try {
            Claims refreshClaims = getRefreshClaims(token);
            if(isExpired(refreshClaims))
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

    // 토큰의 만료 여부를 확인
    private boolean isExpired(Claims claims) {
        return claims.getExpiration().after(new Date());   // 만료 시간이 현재보다 뒤(미래)에 있어야 true
    }

    // token 내에서 userId(subject)를 꺼내오는 메소드
    public String getAccessUsername(String token) {
        Claims claims = getAccessClaims(token);
        return claims.getSubject();
    }
    private Claims getAccessClaims(String token) {
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

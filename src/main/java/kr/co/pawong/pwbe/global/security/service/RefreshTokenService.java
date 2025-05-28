package kr.co.pawong.pwbe.global.security.service;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${spring.security.jwt.refresh-token-validity-in-ms}")
    private long refreshTtlMs;
    private final StringRedisTemplate redisTemplate;

    private String keyFor(Long userId) {
        return "refresh:" + userId;
    }

    // 로그인 시, 새 리프레시 토큰 저장
    public void store(Long userId, String refreshToken) {
        // Redis에 키:userId, 값:token, TTL 설정
        redisTemplate.opsForValue()
                .set(keyFor(userId), refreshToken, Duration.ofMillis(refreshTtlMs));
    }

    // 재발급 시, 기존 토큰과 비교
    public boolean isValid(Long userId, String refreshToken) {
        String saved = redisTemplate.opsForValue().get(keyFor(userId));
        return refreshToken.equals(saved);
    }

    // 토큰 회전: old→new 교체
    public void rotate(Long userId, String newRefreshToken) {
        redisTemplate.opsForValue()
                .set(keyFor(userId), newRefreshToken, Duration.ofMillis(refreshTtlMs));
    }

    // 로그아웃 등: 토큰 삭제
    public void delete(Long userId) {
        redisTemplate.delete(keyFor(userId));
    }

}

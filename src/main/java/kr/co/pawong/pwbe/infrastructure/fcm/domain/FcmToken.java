package kr.co.pawong.pwbe.infrastructure.fcm.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FcmToken {
    private Long id;
    private Long userId;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 토큰 생성
    public static FcmToken create(Long userId, String token) {
        LocalDateTime now = LocalDateTime.now();
        return FcmToken.builder()
                .userId(userId)
                .token(token)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public void updateToken(String token) {
        this.token = token;
        this.updatedAt = LocalDateTime.now();
    }
}

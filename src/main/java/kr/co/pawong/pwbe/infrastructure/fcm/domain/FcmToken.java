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

    // 토큰 정보 업데이트
    public void updateToken(Long userId, String token) {
        this.userId = userId;
        this.token = token;

        // 최초 생성 시에만 createdAt 설정
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }

        // 업데이트 시간 갱신
        this.updatedAt = LocalDateTime.now();
    }
}

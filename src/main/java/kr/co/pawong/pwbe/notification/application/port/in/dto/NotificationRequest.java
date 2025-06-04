package kr.co.pawong.pwbe.notification.application.port.in.dto;

import kr.co.pawong.pwbe.notification.enums.TargetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    private Long userId;
    private String message;
    private Long targetId;
    private TargetType targetType;
    private Long postId;
}

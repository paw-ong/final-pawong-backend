package kr.co.pawong.pwbe.notification.application.service.dto;

import kr.co.pawong.pwbe.notification.enums.NotificationType;
import kr.co.pawong.pwbe.notification.enums.TargetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEmailDto {
    private Long id;
    private Long userId;
    private String title;
    private String message;
    private Long targetId;
    private TargetType targetType;
    private NotificationType type;

}

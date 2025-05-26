package kr.co.pawong.pwbe.Notification.application.service.dto;

import kr.co.pawong.pwbe.Notification.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private Long id;
    private Long userId;
    private String title;
    private String message;
    private String token;
    private Long targetId;
    private NotificationType type;
}

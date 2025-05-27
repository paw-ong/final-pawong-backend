package kr.co.pawong.pwbe.notification.domain;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.NOTIFICATION_MESSAGE_REQUIRED;
import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.NOTIFICATION_TARGET_ID_REQUIRED;
import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.NOTIFICATION_TITLE_REQUIRED;
import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.NOTIFICATION_USER_ID_REQUIRED;

import java.time.LocalDateTime;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import kr.co.pawong.pwbe.notification.application.service.dto.NotificationDto;
import kr.co.pawong.pwbe.notification.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Notification {
    private Long id;
    private Long userId;
    private String title;
    private String message;
    private Long targetId;
    private NotificationType type;
    private LocalDateTime createdAt;

    // 채팅 알림
    public static Notification createChatNotification(Long userId, String message, Long chatId) {
        validateNotificationData(userId, "새로운 채팅 메시지", message, chatId);

        return Notification.builder()
                .userId(userId)
                .title("새로운 메시지")
                .message(message)
                .targetId(chatId)
                .type(NotificationType.CHAT)
                .createdAt(LocalDateTime.now())
                .build();
    }

    // 유사 공고 알림
    public static Notification createSimilarAdoptionNotification(Long userId, Long adoptionId) {
        String title = "유사 공고 발견";
        String message = "유사한 공고가 발견되었습니다.";

        validateNotificationData(userId, title, message, adoptionId);

        return Notification.builder()
                .userId(userId)
                .title(title)
                .message(message)
                .targetId(adoptionId)
                .type(NotificationType.SIMILAR_ADOPTION)
                .createdAt(LocalDateTime.now())
                .build();
    }

    // Notification -> NotificationDto
    public NotificationDto toDto(String token) {
        return NotificationDto.builder()
                .id(this.id)
                .userId(this.userId)
                .token(token)
                .title(this.title)
                .message(this.message)
                .targetId(this.targetId)
                .type(this.type)
                .build();
    }

    // 알림 생성에 필요한 필수 데이터의 유효성 검증
    private static void validateNotificationData(Long userId, String title, String message, Long targetId) {
        if (userId == null) {
            throw new BaseException(NOTIFICATION_USER_ID_REQUIRED);
        }
        if (title == null || title.trim().isEmpty()) {
            throw new BaseException(NOTIFICATION_TITLE_REQUIRED);
        }
        if (message == null || message.trim().isEmpty()) {
            throw new BaseException(NOTIFICATION_MESSAGE_REQUIRED);
        }
        if (targetId == null) {
            throw new BaseException(NOTIFICATION_TARGET_ID_REQUIRED);
        }
    }
}


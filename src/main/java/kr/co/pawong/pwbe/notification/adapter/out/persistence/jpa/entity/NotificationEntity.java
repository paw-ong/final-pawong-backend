package kr.co.pawong.pwbe.notification.adapter.out.persistence.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import kr.co.pawong.pwbe.notification.domain.Notification;
import kr.co.pawong.pwbe.notification.enums.NotificationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@DynamicUpdate
@NoArgsConstructor
@Table(name = "Notifications")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // 사용자 ID

    private String title; // 알림 제목

    private String message; // 알림 메시지

    private Long targetId; // 연관된 대상 ID (채팅방 ID, 동물 ID)

    @Enumerated(EnumType.STRING)
    private NotificationType type; // 알림 유형

    private Long postId;

    private LocalDateTime createdAt; // 알림 생성 시간

    // Notification -> NotificationEntity
    public static NotificationEntity from(Notification notification) {
        NotificationEntity entity = new NotificationEntity();

        entity.id = notification.getId();
        entity.userId = notification.getUserId();
        entity.title = notification.getTitle();
        entity.message = notification.getMessage();
        entity.targetId = notification.getTargetId();
        entity.type = notification.getType();
        entity.postId = notification.getPostId();
        entity.createdAt = notification.getCreatedAt();

        return entity;
    }

    // NotificationEntity -> Notification
    public Notification toModel() {
        return Notification.builder()
                .id(this.id)
                .userId(this.userId)
                .title(this.title)
                .message(this.message)
                .targetId(this.targetId)
                .type(this.type)
                .postId(this.postId)
                .createdAt(this.createdAt)
                .build();
    }
}

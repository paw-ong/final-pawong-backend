package kr.co.pawong.pwbe.lostPost.adapter.out.notification;

import kr.co.pawong.pwbe.lostPost.application.port.out.NotificationSendPort;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import kr.co.pawong.pwbe.notification.application.port.in.NotificationUseCase;
import kr.co.pawong.pwbe.notification.application.port.in.dto.NotificationRequest;
import kr.co.pawong.pwbe.notification.enums.TargetType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationSendAdapter implements NotificationSendPort {

    private final NotificationUseCase notificationUseCase;

    @Override
    public void sendNotification(Long userId, Long targetId, PostType postType) {

        switch (postType) {
            case PostType.FOSTER -> notificationUseCase.sendSimilarAdoptionNotification(
                    new NotificationRequest(userId, null, targetId, TargetType.FOSTER)
            );
            case PostType.FOUND -> notificationUseCase.sendSimilarAdoptionNotification(
                    new NotificationRequest(userId, null, targetId, TargetType.FOUND)
            );
            default -> log.error("[NotificationSendAdapter][sendNotification] 알림 보내는 게시글 타입이 LOST 임. userId={}, targetId={}, postType={}", userId, targetId, postType);
        }
    }
}

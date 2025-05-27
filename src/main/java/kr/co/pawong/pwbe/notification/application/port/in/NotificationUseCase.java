package kr.co.pawong.pwbe.notification.application.port.in;

import java.util.Optional;
import kr.co.pawong.pwbe.notification.application.port.in.dto.NotificationRequest;
import kr.co.pawong.pwbe.notification.application.service.dto.NotificationDto;

public interface NotificationUseCase {
    void sendChatNotification(NotificationRequest request);
    void sendSimilarAdoptionNotification(NotificationRequest request);
    Optional<NotificationDto> getNotification(Long notificationId);

}

package kr.co.pawong.pwbe.Notification.application.port.in;

import java.util.Optional;
import kr.co.pawong.pwbe.Notification.application.port.in.dto.NotificationRequest;
import kr.co.pawong.pwbe.Notification.application.service.dto.NotificationDto;

public interface NotificationUseCase {
    void sendChatNotification(NotificationRequest request);
    void sendSimilarAdoptionNotification(NotificationRequest request);
    Optional<NotificationDto> getNotification(Long notificationId);

}

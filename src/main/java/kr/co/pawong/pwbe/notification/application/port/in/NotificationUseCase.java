package kr.co.pawong.pwbe.notification.application.port.in;

import kr.co.pawong.pwbe.notification.application.port.in.dto.NotificationRequest;
import kr.co.pawong.pwbe.notification.domain.Notification;

public interface NotificationUseCase {
    void sendChatNotification(NotificationRequest request);
    void sendSimilarAdoptionNotification(NotificationRequest request);
    Notification getNotification(Long notificationId);

}

package kr.co.pawong.pwbe.notification.application.port.out;

import kr.co.pawong.pwbe.notification.application.service.dto.NotificationDto;
import kr.co.pawong.pwbe.notification.domain.Notification;

public interface NotificationPort {
    Notification save(Notification notification);
    Notification findById(Long id);
    void sendFcmNotification(NotificationDto notificationDto);
}

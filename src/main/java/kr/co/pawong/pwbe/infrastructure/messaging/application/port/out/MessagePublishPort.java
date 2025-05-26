package kr.co.pawong.pwbe.infrastructure.messaging.application.port.out;

import kr.co.pawong.pwbe.Notification.application.service.dto.NotificationDto;

public interface MessagePublishPort {

    void publishMessage(String topic, Object message);

    void publishFcmNotificationMessage(NotificationDto notificationDto);
}

package kr.co.pawong.pwbe.infrastructure.messaging.application.port.in;

import kr.co.pawong.pwbe.notification.application.service.dto.NotificationDto;

public interface PublishMessageUseCase {

    void publishMessage(String topic, Object message);

    void publishFcmNotificationMessage(NotificationDto notificationDto);
}

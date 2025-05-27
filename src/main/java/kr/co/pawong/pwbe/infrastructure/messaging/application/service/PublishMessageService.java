package kr.co.pawong.pwbe.infrastructure.messaging.application.service;

import kr.co.pawong.pwbe.notification.application.service.dto.NotificationDto;
import kr.co.pawong.pwbe.infrastructure.messaging.application.port.in.PublishMessageUseCase;
import kr.co.pawong.pwbe.infrastructure.messaging.application.port.out.MessagePublishPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublishMessageService implements PublishMessageUseCase {

    private final MessagePublishPort messagePublishPort;

    @Override
    public void publishMessage(String topic, Object message) {
        messagePublishPort.publishMessage(topic, message);
    }

    @Override
    public void publishFcmNotificationMessage(NotificationDto notificationDto) {
        messagePublishPort.publishFcmNotificationMessage(notificationDto);
    }


}

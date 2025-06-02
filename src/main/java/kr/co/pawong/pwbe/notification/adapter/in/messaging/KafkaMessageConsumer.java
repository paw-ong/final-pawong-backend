package kr.co.pawong.pwbe.notification.adapter.in.messaging;

import kr.co.pawong.pwbe.notification.application.port.in.MailUseCase;
import kr.co.pawong.pwbe.notification.application.port.in.NotificationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMessageConsumer {

    private final NotificationUseCase notificationUseCase;
    private final MailUseCase mailUseCase;

    @KafkaListener(
            topics = "${kafka.topic.similar-animal-notification}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeSimilarAnimalNotificationMessage(String jsonString) {
        // TODO: 이후 구현 token 에러로 인해 병렬처리 필요
//        notificationUseCase.processFcmNotificationMessage(jsonString);
        mailUseCase.processMailNotificationMessage(jsonString);
    }

    @KafkaListener(
            topics = "${kafka.topic.chat-notification}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeChatNotificationMessage(String jsonString) {
        notificationUseCase.processFcmNotificationMessage(jsonString);
    }
}

package kr.co.pawong.pwbe.notification.adapter.in.messaging;

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

    @KafkaListener(
            topics = "${kafka.topic.similar-animal-notification}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeSimilarAnimalNotificationMessage(String jsonString) {
        notificationUseCase.processFcmNotificationMessage(jsonString);
    }

    @KafkaListener(
            topics = "${kafka.topic.chat-notification}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeChatNotificationMessage(String jsonString) {
        notificationUseCase.processFcmNotificationMessage(jsonString);
    }

}

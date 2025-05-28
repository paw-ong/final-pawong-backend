package kr.co.pawong.pwbe.lostPost.adapter.out.messaging;

import kr.co.pawong.pwbe.infrastructure.messaging.application.port.in.PublishMessageUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostAnimalMessagePublishPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.dto.CreatedLostAnimalPublishDto;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LostAnimalMessagePublisher implements LostAnimalMessagePublishPort {

    private final String lostPostCreatedTopic;
    private final String rescuedAnimalCreatedTopic;

    private final PublishMessageUseCase publishMessageUseCase;

    public LostAnimalMessagePublisher(
            PublishMessageUseCase publishMessageUseCase,
            @Value("${kafka.topic.lost-post-created}") String lostPostCreatedTopic,
            @Value("${kafka.topic.rescued-animal-created}") String rescuedAnimalCreatedTopic
    ) {
        this.publishMessageUseCase = publishMessageUseCase;
        this.lostPostCreatedTopic = lostPostCreatedTopic;
        this.rescuedAnimalCreatedTopic = rescuedAnimalCreatedTopic;
    }

    @Override
    public void publishLostAnimalCreatedMessage(CreatedLostAnimalPublishDto message) {
        publishCreatedMessageByPostType(message);
    }

    private void publishCreatedMessageByPostType(CreatedLostAnimalPublishDto message) {
        switch (message.type()) {
            case PostType.LOST
                    -> publishMessageUseCase.publishMessage(lostPostCreatedTopic, message);
            case PostType.FOUND, PostType.FOSTER
                    -> publishMessageUseCase.publishMessage(rescuedAnimalCreatedTopic, message);
            default -> log.warn("unexpected type: {}", message.type());
        }
    }
}

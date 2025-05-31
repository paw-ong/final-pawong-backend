package kr.co.pawong.pwbe.lostPost.adapter.out.messaging;

import kr.co.pawong.pwbe.global.config.KafkaTopicConfig;
import kr.co.pawong.pwbe.infrastructure.messaging.application.port.in.PublishMessageUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostAnimalMessagePublishPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.dto.CreatedLostAnimalPublishDto;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LostAnimalMessagePublisher implements LostAnimalMessagePublishPort {

    @Value("${kafka.topic.lost-post-created}")
    private String LOST_POST_CREATED_TOPIC;
    @Value("${kafka.topic.rescued-animal-created}")
    private String RESCUED_ANIMAL_CREATED_TOPIC;

    private final PublishMessageUseCase publishMessageUseCase;

    @Override
    public void publishLostAnimalCreatedMessage(CreatedLostAnimalPublishDto message) {
        switch (message.type()) {
            case PostType.LOST
                    -> publishMessageUseCase.publishMessage(LOST_POST_CREATED_TOPIC, message);
            case PostType.FOUND, PostType.FOSTER
                    -> publishMessageUseCase.publishMessage(RESCUED_ANIMAL_CREATED_TOPIC, message);
        }
    }
}

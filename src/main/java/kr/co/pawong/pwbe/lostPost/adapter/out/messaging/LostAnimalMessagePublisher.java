package kr.co.pawong.pwbe.lostPost.adapter.out.messaging;

import kr.co.pawong.pwbe.global.config.KafkaTopicConfig;
import kr.co.pawong.pwbe.infrastructure.messaging.application.port.in.PublishMessageUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostAnimalMessagePublishPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.dto.CreatedLostAnimalPublishDto;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LostAnimalMessagePublisher implements LostAnimalMessagePublishPort {

    private final PublishMessageUseCase publishMessageUseCase;

    @Override
    public void publishLostAnimalCreatedMessage(CreatedLostAnimalPublishDto message) {
        switch (message.type()) {
            case PostType.LOST
                    -> publishMessageUseCase.publishMessage(KafkaTopicConfig.LOST_POST_CREATED_TOPIC, message);
            case PostType.FOUND, PostType.FOSTER
                    -> publishMessageUseCase.publishMessage(KafkaTopicConfig.RESCUED_ANIMAL_CREATED_TOPIC, message);
        }
    }
}

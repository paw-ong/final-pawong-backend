package kr.co.pawong.pwbe.lostPost.adapter.out.messaging;

import kr.co.pawong.pwbe.global.config.KafkaTopicConfig;
import kr.co.pawong.pwbe.infrastructure.messaging.application.port.in.PublishMessageUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostAnimalMessagePublishPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.dto.CreatedLostAnimalPublishDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LostAnimalMessagePublisher implements LostAnimalMessagePublishPort {

    private final PublishMessageUseCase publishMessageUseCase;

    @Override
    public void publishLostPostCreatedMessage(CreatedLostAnimalPublishDto message) {
        publishMessageUseCase.publishMessage(KafkaTopicConfig.LOST_POST_CREATED_TOPIC, message);
    }

    @Override
    public void publishFoundPostCreatedMessage(CreatedLostAnimalPublishDto message) {
        publishMessageUseCase.publishMessage(KafkaTopicConfig.RESCUED_ANIMAL_CREATED_TOPIC, message);
    }
}

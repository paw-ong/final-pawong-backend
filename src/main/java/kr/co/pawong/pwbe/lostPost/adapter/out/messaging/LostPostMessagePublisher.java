package kr.co.pawong.pwbe.lostPost.adapter.out.messaging;

import kr.co.pawong.pwbe.global.config.KafkaTopicConfig;
import kr.co.pawong.pwbe.infrastructure.messaging.application.port.in.PublishMessageUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostPostMessagePublishPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.dto.CreatedLostPostPublishDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LostPostMessagePublisher implements LostPostMessagePublishPort {

    private final PublishMessageUseCase publishMessageUseCase;

    @Override
    public void publishLostPostCreatedMessage(CreatedLostPostPublishDto message) {
        publishMessageUseCase.publishMessage(KafkaTopicConfig.LOST_POST_CREATED_TOPIC, message);
    }
}

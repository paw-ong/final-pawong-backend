package kr.co.pawong.pwbe.test.application.service;

import kr.co.pawong.pwbe.test.application.port.in.PublishTestMessageUseCase;
import kr.co.pawong.pwbe.test.application.port.out.TestMessagePublisherPort;
import kr.co.pawong.pwbe.test.config.KafkaTopicConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublishTestMessageService implements PublishTestMessageUseCase {

    private final TestMessagePublisherPort testMessagePublisherPort;

    @Override
    public void publishTestMessage(String message) {
        testMessagePublisherPort.publishMessage(KafkaTopicConfig.TEST_TOPIC, message);
    }
}

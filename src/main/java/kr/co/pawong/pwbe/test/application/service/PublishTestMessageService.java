package kr.co.pawong.pwbe.test.application.service;

import kr.co.pawong.pwbe.test.application.port.in.PublishTestMessageUseCase;
import kr.co.pawong.pwbe.test.application.port.out.TestMessagePublishPort;
import kr.co.pawong.pwbe.global.config.KafkaTopicConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublishTestMessageService implements PublishTestMessageUseCase {

    private final TestMessagePublishPort testMessagePublishPort;

    @Override
    public void publishTestMessage(String message) {
        testMessagePublishPort.publishMessage(KafkaTopicConfig.TEST_TOPIC, message);
    }
}

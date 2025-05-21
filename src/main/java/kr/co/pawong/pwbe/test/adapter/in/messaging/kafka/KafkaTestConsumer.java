package kr.co.pawong.pwbe.test.adapter.in.messaging.kafka;

import kr.co.pawong.pwbe.test.application.port.in.PrintLogUseCase;
import kr.co.pawong.pwbe.test.config.KafkaTopicConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaTestConsumer {

    private final PrintLogUseCase printLogUseCase;

    @KafkaListener(topics = KafkaTopicConfig.TEST_TOPIC, groupId = "test-group1")
    public void listen1_1(String message) {
        printLogUseCase.printLog("Received message test1-1: " + message);
    }

    @KafkaListener(topics = KafkaTopicConfig.TEST_TOPIC, groupId = "test-group1")
    public void listen1_2(String message) {
        printLogUseCase.printLog("Received message test1-2: " + message);
    }

    @KafkaListener(topics = KafkaTopicConfig.TEST_TOPIC, groupId = "test-group2")
    public void listen2_1(String message) {
        printLogUseCase.printLog("Received message test2-1: " + message);
    }

    @KafkaListener(topics = KafkaTopicConfig.TEST_TOPIC, groupId = "test-group2")
    public void listen2_2(String message) {
        printLogUseCase.printLog("Received message test2-2: " + message);
    }

    @KafkaListener(topics = KafkaTopicConfig.TEST_TOPIC)
    public void listenInDefaultGroup1(String message) {
        printLogUseCase.printLog("Received message default-1: " + message);
    }

    @KafkaListener(topics = KafkaTopicConfig.TEST_TOPIC)
    public void listenInDefaultGroup2(String message) {
        printLogUseCase.printLog("Received message default-2: " + message);
    }
}

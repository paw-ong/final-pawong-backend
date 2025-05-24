package kr.co.pawong.pwbe.test.adapter.in.messaging.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.pawong.pwbe.test.adapter.in.messaging.kafka.dto.TestMessageConsumeDto;
import kr.co.pawong.pwbe.test.application.port.in.PrintLogUseCase;
import kr.co.pawong.pwbe.global.config.KafkaTopicConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaTestConsumer {

    private final PrintLogUseCase printLogUseCase;
    private final ObjectMapper objectMapper;

    /**
     * 함수 각각이 하나의 컨슈머 입니다.
     * groupId가 같으면 같은 컨슈머 그룹입니다.
     */
    @KafkaListener(topics = KafkaTopicConfig.TEST_TOPIC, groupId = "test-group1")
    public void listen1_1(String message) {
        TestMessageConsumeDto dto = getValue(message);
        printLogUseCase.printLog("Received message test1-1: " + dto);
    }

    @KafkaListener(topics = KafkaTopicConfig.TEST_TOPIC, groupId = "test-group1")
    public void listen1_2(String message) {
        TestMessageConsumeDto dto = getValue(message);
        printLogUseCase.printLog("Received message test1-2: " + dto);
    }

    @KafkaListener(topics = KafkaTopicConfig.TEST_TOPIC, groupId = "test-group2")
    public void listen2_1(String message) {
        TestMessageConsumeDto dto = getValue(message);
        printLogUseCase.printLog("Received message test2-1: " + dto);
    }

    @KafkaListener(topics = KafkaTopicConfig.TEST_TOPIC, groupId = "test-group2")
    public void listen2_2(String message) {
        TestMessageConsumeDto dto = getValue(message);
        printLogUseCase.printLog("Received message test2-2: " + dto);
    }

    @KafkaListener(topics = KafkaTopicConfig.TEST_TOPIC)
    public void listenInDefaultGroup1(String message) {
        TestMessageConsumeDto dto = getValue(message);
        printLogUseCase.printLog("Received message default-1: " + dto);
    }

    @KafkaListener(topics = KafkaTopicConfig.TEST_TOPIC)
    public void listenInDefaultGroup2(String message) {
        TestMessageConsumeDto dto = getValue(message);
        printLogUseCase.printLog("Received message default-2: " + dto);
    }

    private TestMessageConsumeDto getValue(String message) {
        try {
            return objectMapper.readValue(message, TestMessageConsumeDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("[KafkaTestConsumer DTO 변환 에러]", e);
        }
    }
}

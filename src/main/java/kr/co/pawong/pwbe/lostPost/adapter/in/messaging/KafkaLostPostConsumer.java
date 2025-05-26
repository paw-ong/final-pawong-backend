package kr.co.pawong.pwbe.lostPost.adapter.in.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.pawong.pwbe.global.config.KafkaTopicConfig;
import kr.co.pawong.pwbe.lostPost.adapter.in.messaging.dto.EmbeddedLostPostConsumeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaLostPostConsumer {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = KafkaTopicConfig.LOST_POST_EMBEDDED_TOPIC)
    public void lostPostEmbeddedTopicConsumer(String message) {
        try {
            EmbeddedLostPostConsumeDto dto = objectMapper.readValue(message, EmbeddedLostPostConsumeDto.class);
            log.info("[받은 message]: {}", dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("[KafkaLostPostConsumer DTO 변환 에러]", e);
        }
    }
}

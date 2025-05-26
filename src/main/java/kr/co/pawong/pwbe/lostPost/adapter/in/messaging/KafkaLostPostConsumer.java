package kr.co.pawong.pwbe.lostPost.adapter.in.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.pawong.pwbe.global.config.KafkaTopicConfig;
import kr.co.pawong.pwbe.lostPost.adapter.in.messaging.dto.EmbeddedLostPostConsumeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaLostPostConsumer {

    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = KafkaTopicConfig.LOST_POST_EMBEDDED_TOPIC,
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void lostPostEmbeddedTopicConsumer(String message, Acknowledgment ack) {
        try {
            EmbeddedLostPostConsumeDto dto = objectMapper.readValue(message, EmbeddedLostPostConsumeDto.class);
            log.info("[받은 message]: {}", dto);

            // 아래의 acknowledge_가 호출되어야 해당 메시지 오프셋을 읽은 것이 됩니다.
            // -> 호출되지 않으면 해당 메시지를 다시 처리해야 함.
            ack.acknowledge();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("[KafkaLostPostConsumer DTO 변환 에러]", e);
        }
    }
}

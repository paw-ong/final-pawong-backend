package kr.co.pawong.pwbe.infrastructure.messaging.adapter.in;

import kr.co.pawong.pwbe.global.config.KafkaTopicConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaDeadLetterTopicConsumer {

    @KafkaListener(
            topics = KafkaTopicConfig.DEAD_LETTER_TOPIC,
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void lostPostEmbeddedTopicConsumer(String message, Acknowledgment ack) {
        log.error("[DLT]: {}", message);

        ack.acknowledge();
    }
}

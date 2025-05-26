package kr.co.pawong.pwbe.infrastructure.messaging.adapter.in;

import kr.co.pawong.pwbe.global.config.KafkaTopicConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaDeadLetterTopicConsumer {

    /**
     * "[DLT][토픽명][파티션]: 메시지" 형식으로 DLT 메시지를 출력합니다.
     * 이 로그는 dtl.log 파일로 저장됩니다.
     */
    @KafkaListener(
            topics = KafkaTopicConfig.COMMON_DEAD_LETTER_TOPIC
    )
    public void lostPostEmbeddedTopicConsumer(
            ConsumerRecord<String, String> record,
            @Header(KafkaHeaders.DLT_ORIGINAL_TOPIC) String originalTopic,
            @Header(KafkaHeaders.DLT_ORIGINAL_PARTITION) int originalPartition
    ) {
        // 형식: "[DLT][토픽명][파티션]: 메시지"
        log.error("[DLT][{}][{}]: {}", originalTopic, originalPartition, record.value());
    }
}

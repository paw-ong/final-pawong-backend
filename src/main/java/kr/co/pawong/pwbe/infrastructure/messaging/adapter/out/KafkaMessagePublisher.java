package kr.co.pawong.pwbe.infrastructure.messaging.adapter.out;

import kr.co.pawong.pwbe.infrastructure.messaging.application.port.out.MessagePublishPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMessagePublisher implements MessagePublishPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publishMessage(String topic, Object message) {
        kafkaTemplate.send(topic, message);
    }
}

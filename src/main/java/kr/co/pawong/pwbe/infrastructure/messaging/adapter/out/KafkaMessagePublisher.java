package kr.co.pawong.pwbe.infrastructure.messaging.adapter.out;

import kr.co.pawong.pwbe.infrastructure.messaging.application.port.out.MessagePublishPort;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaMessagePublisher implements MessagePublishPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publishMessage(String topic, Object message) {
        kafkaTemplate.send(topic, message);
    }
}

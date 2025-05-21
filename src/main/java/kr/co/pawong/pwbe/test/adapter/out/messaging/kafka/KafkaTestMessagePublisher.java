package kr.co.pawong.pwbe.test.adapter.out.messaging.kafka;

import kr.co.pawong.pwbe.test.application.port.out.TestMessagePublishPort;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaTestMessagePublisher implements TestMessagePublishPort {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void publishMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
}

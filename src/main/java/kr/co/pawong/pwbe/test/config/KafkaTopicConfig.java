package kr.co.pawong.pwbe.test.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {

    public static final String TEST_TOPIC = "test-topic";

    // KafkaAdmin 빈: application.yml 의 spring.kafka.* 설정을 사용
    @Bean
    public KafkaAdmin kafkaAdmin(KafkaProperties props, SslBundles sslBundles) {
        return new KafkaAdmin(props.buildAdminProperties(sslBundles));
    }

    // 애플리케이션 시작 시 my-topic 이 자동 생성됨
    @Bean
    public NewTopic myTopic() {
        return TopicBuilder.name(TEST_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }
}

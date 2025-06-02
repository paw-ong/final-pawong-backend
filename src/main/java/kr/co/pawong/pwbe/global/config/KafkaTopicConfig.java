package kr.co.pawong.pwbe.global.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.topic.similar-animal-notification}")
    private String SIMILAR_ANIMAL_NOTIFICATION_TOPIC;
    @Value("${kafka.topic.chat-notification}")
    private String CHAT_NOTIFICATION_TOPIC;
    @Value("${kafka.topic.common-dead-letter}")
    private String COMMON_DEAD_LETTER_TOPIC;
    @Value("${kafka.topic.lost-post-created}")
    private String LOST_POST_CREATED_TOPIC;
    @Value("${kafka.topic.rescued-animal-created}")
    private String RESCUED_ANIMAL_CREATED_TOPIC;
    @Value("${kafka.topic.lost-post-embedded}")
    private String LOST_POST_EMBEDDED_TOPIC;
    @Value("${kafka.topic.rescued-animal-embedded}")
    private String RESCUED_ANIMAL_EMBEDDED_TOPIC;


    // KafkaAdmin 빈: application.yml 의 spring.kafka.* 설정을 사용
    @Bean
    public KafkaAdmin kafkaAdmin(KafkaProperties props, SslBundles sslBundles) {
        return new KafkaAdmin(props.buildAdminProperties(sslBundles));
    }

    /**
     * 트랜잭션 실패하고 재시도에도 실패한 메시지들이 오는 토픽
     */
    @Bean
    public NewTopic deadLetterTopic() {
        return TopicBuilder.name(COMMON_DEAD_LETTER_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic lostPostCreatedTopic() {
        return TopicBuilder.name(LOST_POST_CREATED_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic rescuedAnimalCreatedTopic() {
        return TopicBuilder.name(RESCUED_ANIMAL_CREATED_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic lostPostEmbeddedTopic() {
        return TopicBuilder.name(LOST_POST_EMBEDDED_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic rescuedAnimalEmbeddedTopic() {
        return TopicBuilder.name(RESCUED_ANIMAL_EMBEDDED_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    // fcm 알림
    @Bean
    public NewTopic similarAnimalNotificationTopic() {
        return TopicBuilder.name(SIMILAR_ANIMAL_NOTIFICATION_TOPIC)
                .partitions(3) // 토픽의 파티션 수
                .replicas(1) // 각 파티션의 복제본 수
                .build();
    }

    @Bean NewTopic chatNotificationTopic() {
        return TopicBuilder.name(CHAT_NOTIFICATION_TOPIC)
                .partitions(3) // 토픽의 파티션 수
                .replicas(1) // 각 파티션의 복제본 수
                .build();
    }
}

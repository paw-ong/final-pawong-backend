package kr.co.pawong.pwbe.global.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {

    public static final String COMMON_DEAD_LETTER_TOPIC = "dev.pawong.common.dlt";

    public static final String LOST_POST_CREATED_TOPIC = "dev.pawong.lost-post.created";
    public static final String RESCUED_ANIMAL_CREATED_TOPIC = "dev.pawong.rescued-animal.created";
    public static final String LOST_POST_EMBEDDED_TOPIC = "dev.pawong.lost-post.embedded";
    public static final String RESCUED_ANIMAL_EMBEDDED_TOPIC = "dev.pawong.rescued-animal.embedded";

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
}

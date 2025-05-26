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

    public static final String LOST_POST_CREATED_TOPIC = "dev.pawong.lost-post.created";
    public static final String RESCUED_ANIMAL_CREATED_TOPIC = "dev.pawong.rescued-animal.created";
    public static final String LOST_POST_EMBEDDED_TOPIC = "dev.pawong.lost-post.embedded";
    public static final String RESCUED_ANIMAL_EMBEDDED_TOPIC = "dev.pawong.rescued-animal.embedded";
    public static final String LOST_POST_INDEXED_TOPIC = "dev.pawong.lost-post.indexed";
    public static final String RESCUED_ANIMAL_INDEXED_TOPIC = "dev.pawong.rescued-animal.indexed";

    @Bean
    public KafkaAdmin kafkaAdmin(KafkaProperties props, SslBundles sslBundles) {
        return new KafkaAdmin(props.buildAdminProperties(sslBundles));
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

    @Bean
    public NewTopic lostPostIndexedTopic() {
        return TopicBuilder.name(LOST_POST_INDEXED_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic rescuedAnimalIndexedTopic() {
        return TopicBuilder.name(RESCUED_ANIMAL_INDEXED_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }
}

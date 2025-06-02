package kr.co.pawong.pwbe.lostPost.adapter.in.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.pawong.pwbe.global.config.KafkaTopicConfig;
import kr.co.pawong.pwbe.lostPost.adapter.in.messaging.dto.EmbeddedLostAnimalConsumeDto;
import kr.co.pawong.pwbe.lostPost.application.port.in.NotifyUsersOfSimilarLostPostsUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.StreamSimilarAnimalsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaLostAnimalConsumer {

    private final ObjectMapper objectMapper;
    private final StreamSimilarAnimalsUseCase streamSimilarAnimalsUseCase;
    private final NotifyUsersOfSimilarLostPostsUseCase notifyUsersOfSimilarLostPostsUseCase;

    /**
     * "실종" 신고 게시물 임베딩이 끝난 경우 호출
     */
    @KafkaListener(
            topics = "${kafka.topic.lost-post-embedded}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void lostPostEmbeddedTopicConsumer(String message) throws JsonProcessingException {
        log.info("LOST_POST_EMBEDDED_TOPIC 이벤트 수신");
        EmbeddedLostAnimalConsumeDto dto = objectMapper
                .readValue(message, EmbeddedLostAnimalConsumeDto.class);

        streamSimilarAnimalsUseCase.streamSimilarAnimals(dto.id(), dto.type(), dto.embedding());
    }

    /**
     * "발견" 게시물 또는 "구조"되어서 보호소에 보호중인 실종 동물 임베딩이 끝난 경우 호출
     */
    @KafkaListener(
            topics = "${kafka.topic.rescued-animal-embedded}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void rescuedAnimalEmbeddedTopicConsumer(String message) throws JsonProcessingException {
        log.info("RESCUED_ANIMAL_EMBEDDED_TOPIC 이벤트 수신");
        EmbeddedLostAnimalConsumeDto dto = objectMapper
                .readValue(message, EmbeddedLostAnimalConsumeDto.class);

        notifyUsersOfSimilarLostPostsUseCase.notifyUsersOfSimilarLostPosts(dto.id(), dto.type(),
                dto.embedding());
    }
}

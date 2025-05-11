package kr.co.pawong.pwbe.infrastructure.ai;

import java.util.List;
import java.util.Optional;
import kr.co.pawong.pwbe.adoption.application.port.out.AdoptionAiPort;
import kr.co.pawong.pwbe.infrastructure.ai.port.ChatProcessorPort;
import kr.co.pawong.pwbe.infrastructure.ai.port.EmbeddingProcessorPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Adoption에서 사용하는 AI 관련 기능을 제공합니다.
 *
 * @exception: null이나 빈 문자열, 공백이 입력된 경우 IllegalArgumentException을 던집니다.
 */
@Service
@Slf4j
public class AdoptionAiService implements AdoptionAiPort {

    public static final int EMBEDDING_DIMENSION = 1536;

    private final EmbeddingProcessorPort embeddingPort;
    private final ChatProcessorPort chatPort;
    private final AdoptionAiExecutor executor;

    // 문장 정제하는 함수
    @Override
    public String refineSpecialMark(String specialMark) {
        if (!isValidateInput(specialMark)) {
            return "";
        }
        return chatPort.refineAdoptionSentence(specialMark);
    }

    // 위의 함수를 병렬로 수행하는 함수
    @Override
    public List<Optional<String>> refineSpecialMarkBatch(List<String> specialMarks) {
        return executor.run(specialMarks, this::refineSpecialMark);
    }

    // 입력된 문장에 대해 태그를 선택해서 문자열 리스트로 반환하는 함수
    @Override
    public List<String> tag(String feature) {
        if (!isValidateInput(feature)) {
            return List.of();
        }
        return chatPort.getTagsByFeature(feature);
    }

    // 위의 함수를 병렬로 수행하는 함수
    @Override
    public List<Optional<List<String>>> tagBatch(List<String> features) {
        return executor.run(features, this::tag);
    }

    // 문장 임베딩하는 함수
    @Override
    public float[] embed(String completion) {
        if (!isValidateInput(completion)) {
            completion = " "; // 입력이 없을 시에 공백을 임베딩함.
        }
        return embeddingPort.embed(completion);
    }

    // 위의 함수를 병렬로 수행하는 함수
    @Override
    public List<Optional<float[]>> embedBatch(List<String> completions) {
        return executor.run(completions, this::embed);
    }

    // 입력값 검증하는 함수
    private boolean isValidateInput(String input) {
        return input != null && !input.isBlank();
    }


    // 임베딩할 때 사용할 구현체 설정
    private final String EMBEDDING_BEAN_NAME = "OPENAI_EMBEDDING";
    // AI 채팅할 때 사용할 구현체 설정
    private final String CHAT_BEAN_NAME = "OPENAI_CHAT";

    public AdoptionAiService(
            @Qualifier(EMBEDDING_BEAN_NAME) EmbeddingProcessorPort embeddingPort,
            @Qualifier(CHAT_BEAN_NAME) ChatProcessorPort chatPort,
            AdoptionAiExecutor adoptionAiExecutor
    ) {
        this.embeddingPort = embeddingPort;
        this.chatPort = chatPort;
        this.executor = adoptionAiExecutor;
    }

}

package kr.co.pawong.pwbe.infrastructure.ai.adapter.openai;

import static kr.co.pawong.pwbe.infrastructure.ai.enums.RefineMsg.REFINE_TEMPLATE_1;
import static kr.co.pawong.pwbe.infrastructure.ai.enums.TaggingMsg.TAGGING_TEMPLATE_1;
import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.SEARCH_ERROR;

import java.util.List;
import kr.co.pawong.pwbe.infrastructure.ai.port.ChatProcessorPort;
import kr.co.pawong.pwbe.infrastructure.ai.enums.AnimalFeature;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 사용자 입력(프롬프트)을 기반으로 GPT 응답을 생성하는 기능을 제공합니다.
 */
@Slf4j
@Component
@Qualifier("OPENAI_CHAT")
@RequiredArgsConstructor
public class OpenAiChatAdapter implements ChatProcessorPort {

    private final OpenAiChatModel chatModel;

    @Override
    public String queryByPrompt(String prompt) {
        return getCompletion(prompt);
    }

    @Override
    public String refineAdoptionSentence(String sentence) {
        String refinedStr = getCompletion(REFINE_TEMPLATE_1.getMessage(sentence));
        log.info("refinedSpecialMark: {}", refinedStr);
        return refinedStr;
    }

    @Override
    public List<String> getTagsByFeature(String feature) {
        String tagsStr = getCompletion(TAGGING_TEMPLATE_1.getMessage(feature));
        log.info("tagsField: {}", tagsStr);
        return AnimalFeature.extractValidTags(tagsStr);
    }

    private String getCompletion(String sentence) {
        try {
            return chatModel.call(sentence);
        } catch (Exception e) {
            throw new BaseException(SEARCH_ERROR, e);
        }
    }
}
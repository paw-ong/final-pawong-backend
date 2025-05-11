package kr.co.pawong.pwbe.infrastructure.ai.huggingface;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.SEARCH_ERROR;

import kr.co.pawong.pwbe.infrastructure.ai.port.EmbeddingProcessorPort;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
@Qualifier("HUGGINGFACE_EMBEDDING")
@RequiredArgsConstructor
public class HuggingFaceEmbeddingAdapter implements EmbeddingProcessorPort {

    private final HuggingFaceEmbeddingModel embeddingModel;

    @Override
    public @Nullable float[] embed(String completion) {
        return getEmbedding(completion);
    }

    private float[] getEmbedding(String completion) {
        try {
            return embeddingModel.embed(completion);
        } catch (Exception e) {
            throw new BaseException(SEARCH_ERROR, e);
        }
    }
}

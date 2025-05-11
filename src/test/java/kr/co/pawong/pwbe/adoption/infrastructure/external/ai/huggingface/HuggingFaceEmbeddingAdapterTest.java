package kr.co.pawong.pwbe.adoption.infrastructure.external.ai.huggingface;

import kr.co.pawong.pwbe.infrastructure.ai.huggingface.HuggingFaceEmbeddingAdapter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@Disabled("AI 연동 테스트는 전체 빌드 시 제외")
@SpringBootTest(properties = "spring.profiles.active=dev")
class HuggingFaceEmbeddingAdapterTest {

    @Autowired
    private HuggingFaceEmbeddingAdapter embeddingAdapter;

    @Test
    void 임베딩_되나_확인() {
        float[] output = embeddingAdapter.embed("새끼 강아지인데 어르신들과 잘 지내고 순종적인");
        System.out.println("벡터 차원: " + (output == null ? null : output.length));
        System.out.println("벡터: " + Arrays.toString(output));
    }
}
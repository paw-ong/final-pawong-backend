package kr.co.pawong.pwbe.adoption.infrastructure.external.ai.openai;

import kr.co.pawong.pwbe.infrastructure.ai.openai.OpenAiEmbeddingAdapter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

/**
 * infrastructure라.. 스프링 부트 없이 테스트가 안 될 것 같아요. 비활성화 해놓겠습니다.
 */
//@Disabled("AI 연동 테스트는 전체 빌드 시 제외")
@SpringBootTest(properties = "spring.profiles.active=dev")
class OpenAiEmbeddingAdapterTest {

    @Autowired
    private OpenAiEmbeddingAdapter openAiEmbeddingAdapter;

    @Test
    void 임베딩_되나_확인() {
        float[] output = openAiEmbeddingAdapter.embed(" ");
        System.out.println("벡터 차원: " + output.length);
        System.out.println("벡터: " + Arrays.toString(output));
    }
}
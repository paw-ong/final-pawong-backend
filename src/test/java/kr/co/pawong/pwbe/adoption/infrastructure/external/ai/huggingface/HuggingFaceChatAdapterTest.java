package kr.co.pawong.pwbe.adoption.infrastructure.external.ai.huggingface;

import kr.co.pawong.pwbe.infrastructure.ai.huggingface.HuggingFaceChatAdapter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Disabled("AI 연동 테스트는 전체 빌드 시 제외")
@SpringBootTest(properties = "spring.profiles.active=dev")
class HuggingFaceChatAdapterTest {

    @Autowired
    private HuggingFaceChatAdapter chatAdapter;

    @Test
    void LLM_질의() {
        String output = chatAdapter.queryByPrompt("너 모델 이름 뭐야?");
        System.out.println(output);
    }

    @Test
    void 문장_전처리() {
        String searchTerm = "새낑 강아지인데 어르신들과 잘 지냄. 구조? 순종적인? 아아아아 졸리다";
        String output = chatAdapter.refineAdoptionSentence(searchTerm);
        System.out.println(output);
    }

    @Test
    void 태깅() {
        String searchTerm = "새낑 강아지인데 어르신들과 잘 지냄. 구조? 순종적인?";
        List<String> output = chatAdapter.getTagsByFeature(searchTerm);
        System.out.println(output);
    }
}
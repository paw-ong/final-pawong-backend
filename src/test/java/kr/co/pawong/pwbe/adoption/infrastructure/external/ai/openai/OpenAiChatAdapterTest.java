package kr.co.pawong.pwbe.adoption.infrastructure.external.ai.openai;

import kr.co.pawong.pwbe.infrastructure.ai.openai.OpenAiChatAdapter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Disabled("AI 연동 테스트는 전체 빌드 시 제외")
@SpringBootTest(properties = "spring.profiles.active=dev")
class OpenAiChatAdapterTest {

    @Autowired
    private OpenAiChatAdapter chatAdapter;

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
        String animalFeature = "새낑 강아지인데 어르신들과 잘 지냄. 구조? 순종적인? / 체중: 7kg / 색: 검정 / 나이 9살";
        List<String> output = chatAdapter.getTagsByFeature(animalFeature);
        System.out.println(output);
        // 예상 출력: [노인과잘지냄, 무게가중간임, 색깔이어두움, 나이가많음, 구조됨, 온순함]
    }
}
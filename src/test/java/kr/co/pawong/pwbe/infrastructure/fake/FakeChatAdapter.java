package kr.co.pawong.pwbe.infrastructure.fake;

import kr.co.pawong.pwbe.infrastructure.ai.port.ChatProcessorPort;

import java.util.List;

public class FakeChatAdapter implements ChatProcessorPort {
    @Override
    public String queryByPrompt(String prompt) {
        if (prompt.equals("fail"))
            throw new RuntimeException("fail query");
        return "AI 대답";
    }

    @Override
    public String refineAdoptionSentence(String sentence) {
        if (sentence.equals("fail"))
            throw new RuntimeException("fail refine");
        return "test";
    }

    @Override
    public List<String> getTagsByFeature(String feature) {
        if (feature.equals("fail"))
            throw new RuntimeException("fail tagging");
        return List.of("정이많음", "사람을좋아함");
    }
}


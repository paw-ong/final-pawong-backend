package kr.co.pawong.pwbe.infrastructure.ai.port;

import java.util.List;

public interface ChatProcessorPort {

    /**
     * 범용 LLM 질의용 함수.
     * @param prompt - LLM에 질문할 프롬프트 문자열
     * @return LLM 응답
     */
    String queryByPrompt(String prompt);

    /**
     * 문장에 유기 동물과 관련된 정보만 남게 정제합니다.
     * @param sentence - 문장
     * @return 유기 동물 정보만 남은 정제된 문자열
     */
    String refineAdoptionSentence(String sentence);

    /**
     * 동물 특징을 기반으로 태깅해서 문자열 리스트를 반환합니다.
     * @param feature - 동물 특징 문자열
     * @return 태그 문자열 리스트
     */
    List<String> getTagsByFeature(String feature);
}

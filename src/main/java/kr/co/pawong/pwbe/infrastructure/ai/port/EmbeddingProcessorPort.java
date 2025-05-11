package kr.co.pawong.pwbe.infrastructure.ai.port;

public interface EmbeddingProcessorPort {

    /**
     * 문장을 임베딩합니다. 차원(배열 길이)은 모델마다 다릅니다.
     * @param completion - 임베딩할 문장
     * @return 임베딩 된 후의 벡터
     */
    float[] embed(String completion);
}

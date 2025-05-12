package kr.co.pawong.pwbe.infrastructure.fake;

import kr.co.pawong.pwbe.infrastructure.ai.port.EmbeddingProcessorPort;

public class FakeEmbeddingAdapter implements EmbeddingProcessorPort {
    @Override
    public float[] embed(String completion) {
        if (completion.equals("fail"))
            throw new RuntimeException("fail embed");
        return new float[]{1.01f, 1.02f};
    }
}

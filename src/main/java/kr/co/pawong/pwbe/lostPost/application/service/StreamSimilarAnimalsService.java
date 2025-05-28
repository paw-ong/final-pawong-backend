package kr.co.pawong.pwbe.lostPost.application.service;

import kr.co.pawong.pwbe.lostPost.adapter.in.sse.event.LostPostIndexedEvent;
import kr.co.pawong.pwbe.lostPost.application.port.in.IndexLostAnimalUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.StreamSimilarAnimalsUseCase;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StreamSimilarAnimalsService implements StreamSimilarAnimalsUseCase {

    private final ApplicationEventPublisher publisher;

    private final IndexLostAnimalUseCase indexLostAnimalUseCase;

    @Override
    public void streamSimilarAnimals(long id, PostType type, float[] embedding) {
        // 인덱싱
        indexLostAnimalUseCase.index(id, type, embedding);

        // SSE 전송을 위해 이벤트 실종 동물 인덱싱 완료 이벤트 발행
        publisher.publishEvent(new LostPostIndexedEvent(this, id));
    }
}

package kr.co.pawong.pwbe.lostPost.application.service;

import kr.co.pawong.pwbe.lostPost.application.port.in.IndexLostAnimalUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.StreamSimilarAnimalsUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostAnimalEngineCommandPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostPostDataQueryPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.dto.LostAnimalDto;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StreamSimilarAnimalsService implements StreamSimilarAnimalsUseCase {

    private final IndexLostAnimalUseCase indexLostAnimalUseCase;

    @Override
    public void streamSimilarAnimals(long id, PostType type, float[] embedding) {
        indexLostAnimalUseCase.index(id, type, embedding);
    }
}

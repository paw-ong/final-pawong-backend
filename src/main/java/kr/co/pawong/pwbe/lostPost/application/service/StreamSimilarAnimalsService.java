package kr.co.pawong.pwbe.lostPost.application.service;

import kr.co.pawong.pwbe.lostPost.application.port.in.StreamSimilarAnimalsUseCase;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StreamSimilarAnimalsService implements StreamSimilarAnimalsUseCase {

    @Override
    public void streamSimilarAnimals(long id, PostType type, float[] embedding) {

    }
}

package kr.co.pawong.pwbe.lostPost.application.service;

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

    private final LostPostDataQueryPort lostPostDataQueryPort;
    private final LostAnimalEngineCommandPort lostAnimalEngineCommandPort;

    @Override
    public void streamSimilarAnimals(long id, PostType type, float[] embedding) {
        indexing(id, type, embedding);
    }

    private void indexing(long id, PostType type, float[] embedding) {
        if (type != PostType.LOST) {
            throw new IllegalStateException("Unexpected PostType: " + type.name());
        }
        // LOST 게시물 데이터 가져오기
        LostPost lostPost = lostPostDataQueryPort.findLostPostByIdOrThrow(id);

        // lostPost -> lostAnimalDto
        LostAnimalDto lostPostDto = LostAnimalDto.fromLostPost(lostPost, type, embedding);

        // ES에 저장
        lostAnimalEngineCommandPort.saveLostAnimalToEs(lostPostDto);
    }
}

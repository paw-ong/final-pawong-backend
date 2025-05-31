package kr.co.pawong.pwbe.lostPost.application.service;

import kr.co.pawong.pwbe.lostPost.application.port.in.IndexLostAnimalUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostAdoptionDataQueryPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostAnimalEngineCommandPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostPostDataQueryPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.dto.LostAnimalDto;
import kr.co.pawong.pwbe.lostPost.domain.LostAdoption;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IndexLostAnimalService implements IndexLostAnimalUseCase {


    private final LostPostDataQueryPort lostPostDataQueryPort;
    private final LostAdoptionDataQueryPort lostAdoptionDataQueryPort;
    private final LostAnimalEngineCommandPort lostAnimalEngineCommandPort;

    @Override
    public void index(long id, PostType type, float[] embedding) {
        switch (type) {
            case PostType.FOUND, PostType.LOST -> {
                // 발견 게시물 데이터 가져오기
                LostPost lostPost = lostPostDataQueryPort.findLostPostByIdOrThrow(id);

                // lostPost -> lostAnimalDto
                LostAnimalDto lostPostDto = LostAnimalDto.fromLostPost(lostPost, type, embedding);

                // ES에 저장
                lostAnimalEngineCommandPort.saveLostAnimalToEs(lostPostDto);
            }
            case PostType.FOSTER -> {
                // 구조 API 데이터 가져오기
                LostAdoption lostAdoption = lostAdoptionDataQueryPort.findAdoptionByIdOrThrow(id);

                // lostAdoption -> lostAnimalDto
                LostAnimalDto lostAdoptionDto = LostAnimalDto.fromLostAnimal(lostAdoption, type,
                        embedding);

                // ES에 저장
                lostAnimalEngineCommandPort.saveLostAnimalToEs(lostAdoptionDto);
            }
            default -> throw new IllegalStateException("Unexpected PostType: " + type.name());
        }
    }
}

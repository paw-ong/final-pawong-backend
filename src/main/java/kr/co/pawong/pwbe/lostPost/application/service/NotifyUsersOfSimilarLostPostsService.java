package kr.co.pawong.pwbe.lostPost.application.service;

import java.util.List;
import kr.co.pawong.pwbe.lostPost.application.port.in.NotifyUsersOfSimilarLostPostsUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.QueryLostPostDataUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.SearchLostAnimalEngineUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAnimalEngineRequest;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAnimalEngineResponse;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAnimalQuery;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostAdoptionDataQueryPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostAnimalEngineCommandPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostPostDataQueryPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.dto.LostAnimalDto;
import kr.co.pawong.pwbe.lostPost.domain.LostAdoption;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyUsersOfSimilarLostPostsService implements NotifyUsersOfSimilarLostPostsUseCase {

    private final LostPostDataQueryPort lostPostDataQueryPort;
    private final LostAdoptionDataQueryPort lostAdoptionDataQueryPort;
    private final LostAnimalEngineCommandPort lostAnimalEngineCommandPort;

    private final SearchLostAnimalEngineUseCase searchLostAnimalEngineUseCase;
    private final QueryLostPostDataUseCase queryLostPostDataUseCase;

    /**
     * 1. DB 조회 후에 데이터 ES_에 인덱싱
     * 2. 현재 게시글과 유사한 LOST 타입 문서를 ES_에서 검색
     * 3. 해당 실종 게시글 작성자에게 알림
     */
    @Override
    public void notifyUsersOfSimilarLostPosts(long id, PostType type, float[] embedding) {
        // 1. DB 조회 후에 데이터 ES_에 인덱싱
        indexing(id, type, embedding);

        // 2. ES 검색
        List<LostAnimalEngineResponse> similarAnimals = searchLostAnimalEngineUseCase.searchSimilarLostAnimals(
                new LostAnimalEngineRequest(List.of(PostType.LOST), embedding)
        );

        // 3. 작성자 정보 가져오기
        List<Long> userIds = queryLostPostDataUseCase.getUserIdsByLostPostIds(
                similarAnimals.stream().map(LostAnimalEngineResponse::id).toList());

        // 4. 알림 호출

    }

    private void indexing(long id, PostType type, float[] embedding) {
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

package kr.co.pawong.pwbe.lostPost.application.service;

import java.util.List;
import kr.co.pawong.pwbe.lostPost.application.port.in.IndexLostAnimalUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.NotifyUsersOfSimilarLostPostsUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.QueryLostPostDataUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostAnimalEngineQueryPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.dto.LostAnimalEngineRequest;
import kr.co.pawong.pwbe.lostPost.application.port.out.dto.LostAnimalEngineResponse;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyUsersOfSimilarLostPostsService implements NotifyUsersOfSimilarLostPostsUseCase {

    private final LostAnimalEngineQueryPort lostAnimalEngineQueryPort;

    private final QueryLostPostDataUseCase queryLostPostDataUseCase;
    private final IndexLostAnimalUseCase indexLostAnimalUseCase;

    /**
     * 1. DB 조회 후에 데이터 ES_에 인덱싱
     * 2. 현재 게시글과 유사한 LOST 타입 문서를 ES_에서 검색
     * 3. 해당 실종 게시글 작성자에게 알림
     */
    @Override
    public void notifyUsersOfSimilarLostPosts(long id, PostType type, float[] embedding) {
        // 1. DB 조회 후에 데이터 ES_에 인덱싱
        indexLostAnimalUseCase.index(id, type, embedding);

        // 2. ES 검색
        List<LostAnimalEngineResponse> similarAnimals = lostAnimalEngineQueryPort.searchSimilarLostAnimals(
                new LostAnimalEngineRequest(List.of(PostType.LOST), embedding)
        );

        // 3. 작성자 정보 가져와서 알림 호출
        for (LostAnimalEngineResponse similarAnimal : similarAnimals) {
            Long userId = queryLostPostDataUseCase.getUserIdByLostPostId(similarAnimal.id());
        }

    }
}

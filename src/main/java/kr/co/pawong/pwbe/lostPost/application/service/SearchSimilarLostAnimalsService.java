package kr.co.pawong.pwbe.lostPost.application.service;

import java.util.List;
import kr.co.pawong.pwbe.lostPost.application.port.in.PublishCreatedLostAnimalUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.QueryLostAnimalDataUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.QueryLostPostDataUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.SearchSimilarLostAnimalsUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAnimalQuery;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAnimalQuery.LostType;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostDetailDto;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostAnimalEngineQueryPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.dto.LostAnimalEngineResponse;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchSimilarLostAnimalsService implements SearchSimilarLostAnimalsUseCase {

    private final LostAnimalEngineQueryPort lostAnimalEngineQueryPort;

    private final QueryLostAnimalDataUseCase queryLostAnimalDataUseCase;
    private final QueryLostPostDataUseCase queryLostPostDataUseCase;
    private final PublishCreatedLostAnimalUseCase publishCreatedLostAnimalUseCase;

    @Override
    public List<LostPostCard> searchSimilarLostAnimals(Long lostPostId) {
        // 유사 LostAnimal 조회
        List<LostAnimalEngineResponse> result = lostAnimalEngineQueryPort.searchSimilarLostAnimalsByESId(
                PostType.LOST.name() + "_" + lostPostId,
                List.of(PostType.FOUND, PostType.FOSTER)
        );
        // null_일 경우 아직 해당 데이터가 인덱싱되지 않은 것.
        if (result == null) {
            publishNotIndexedLostPost(lostPostId);
            return null;
        }
        // RDB 조회해서 결과 반환
        return queryLostAnimalDataUseCase.getLostAnimalsByIds(
                result.stream()
                        .map(response -> {
                            LostType lostType = response.type() == PostType.FOSTER ? LostType.LOST_ADOPTION : LostType.LOST_POST;
                            return new LostAnimalQuery(lostType, response.id(), null);
                        })
                        .toList()
        );
    }

    /**
     * 인덱싱이 안된 경우 새로 임베딩 메시지를 발행합니다.
     */
    private void publishNotIndexedLostPost(long lostPostId) {
        LostPostDetailDto lostPostDetail = queryLostPostDataUseCase.findLostPostById(lostPostId);
        if (lostPostDetail.getPostType() != PostType.LOST) {
            return;
        }
        String textFeature = String.join(" ",
                lostPostDetail.getColor(),
                lostPostDetail.getKindNm(),
                lostPostDetail.getUpKindNm().name());
        publishCreatedLostAnimalUseCase.publishCreatedLostAnimal(
                lostPostId, lostPostDetail.getPostType(), textFeature, lostPostDetail.getImageUrl().toString()
        );
    }
}

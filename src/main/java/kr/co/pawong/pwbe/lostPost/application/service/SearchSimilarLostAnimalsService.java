package kr.co.pawong.pwbe.lostPost.application.service;

import java.util.List;
import kr.co.pawong.pwbe.lostPost.application.port.in.QueryLostAnimalDataUseCase;
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

    @Override
    public List<LostPostCard> searchSimilarLostAnimals(Long userId, Long lostPostId) {
        // 유사 LostAnimal 조회
        List<LostAnimalEngineResponse> result = lostAnimalEngineQueryPort.searchSimilarLostAnimalsByESId(
                PostType.LOST.name() + "_" + lostPostId,
                List.of(PostType.FOUND, PostType.FOSTER)
        );
        // null_일 경우 아직 해당 데이터가 인덱싱되지 않은 것.
        if (result == null) {
            return null;
        }
        // RDB 조회해서 결과 반환
        return queryLostAnimalDataUseCase.getLostAnimalsByIds(
                result.stream()
                        .map(response -> {
                            LostType lostType = response.type() == PostType.FOSTER ? LostType.LOST_ADOPTION : LostType.LOST_POST;
                            return new LostAnimalQuery(lostType, response.id(), userId);
                        })
                        .toList()
        );
    }
}

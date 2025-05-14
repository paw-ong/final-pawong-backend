package kr.co.pawong.pwbe.user.adapter.out.lostPost;

import java.util.List;
import kr.co.pawong.pwbe.lostPost.application.port.in.QueryLostPostDataUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAnimalQuery;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import kr.co.pawong.pwbe.user.adapter.out.lostPost.mapper.LostAnimalQueryMapper;
import kr.co.pawong.pwbe.user.application.port.out.LostPostInfoPort;
import kr.co.pawong.pwbe.user.application.port.out.dto.MyPageLostPostInfo;
import kr.co.pawong.pwbe.user.application.port.out.mapper.LostPostMapper;
import kr.co.pawong.pwbe.user.domain.LostBookmark;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LostPostInfoAdapter implements LostPostInfoPort {

    private final QueryLostPostDataUseCase queryLostPostDataUseCase;

    @Override
    @Transactional(readOnly = true)
    public List<MyPageLostPostInfo> getLostPostsByUserId(Long userId) {
        List<LostPostCard> lostPostCards = queryLostPostDataUseCase.getLostPostsByUserId(userId);

        return lostPostCards.stream()
                .map(LostPostMapper::toMyPostLostPostInfo)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MyPageLostPostInfo> getLostAnimalsByLostPostIds(List<LostBookmark> lostPostIds) {
        // dto 변환
        List<LostAnimalQuery> queryDtoList = lostPostIds.stream()
                .map(LostAnimalQueryMapper::toLostAnimalQuery)
                .toList();
        // LostPost 도메인으로 요청
        List<LostPostCard> lostPostCards = queryLostPostDataUseCase.getLostAnimalsByPostIds(
                queryDtoList);
        return lostPostCards.stream()
                .map(LostPostMapper::toMyPostLostPostInfo)
                .toList();

    }

}

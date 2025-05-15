package kr.co.pawong.pwbe.user.adapter.out.lostPost;

import java.util.List;
import kr.co.pawong.pwbe.lostPost.application.port.in.QueryLostPostDataUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import kr.co.pawong.pwbe.user.application.port.out.LostPostInfoPort;
import kr.co.pawong.pwbe.user.application.port.out.dto.MyPageLostPostInfo;
import kr.co.pawong.pwbe.user.application.port.out.mapper.LostPostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LostPostInfoAdapter implements LostPostInfoPort {

    private final QueryLostPostDataUseCase queryLostPostDataUseCase;

    @Override
    public List<MyPageLostPostInfo> getLostPostsByUserId(Long userId) {
        List<LostPostCard> lostPostCards = queryLostPostDataUseCase.getLostPostsByUserId(userId);

        return lostPostCards.stream()
                .map(LostPostMapper::toMyPostLostPostInfo)
                .toList();
    }
}

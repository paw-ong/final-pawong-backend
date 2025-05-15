package kr.co.pawong.pwbe.lostPost.application.service;

import java.time.Clock;
import java.util.List;
import java.util.stream.Collectors;
import kr.co.pawong.pwbe.lostPost.application.port.in.QueryLostPostDataUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.SliceLostPostSearchResponses;
import kr.co.pawong.pwbe.lostPost.application.port.in.mapper.LostPostCardMapper;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostPostDataQueryPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.UserInfoPort;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QueryLostPostDataService implements QueryLostPostDataUseCase {

    private final LostPostDataQueryPort lostPostDataQueryPort;
    private final UserInfoPort userInfoPort;
    private final Clock clock;

    @Override
    @Transactional(readOnly = true)
    public List<LostPostCard> getLostPostsByUserId(Long userId) {

        List<LostPost> lostPosts = lostPostDataQueryPort.getLostPostsByUserId(userId);
        String author = userInfoPort.getNicknameByUserId(userId);

        return lostPosts.stream()
                .map(post -> LostPostCardMapper.toLostPostCard(post, author, clock))
                .toList();
    }

    @Override
    public SliceLostPostSearchResponses fetchSlicedLostPosts(Pageable pageable, PostType type) {
        Page<LostPost> lostPostPage = lostPostDataQueryPort.getLostPostsByPostTypePaged(pageable, type);
        List<LostPostCard> lostPostCards = mapToLostPostCards(lostPostPage,clock);
        boolean hasNext = lostPostPage.hasNext();
        return new SliceLostPostSearchResponses(hasNext,lostPostCards);
    }

    private List<LostPostCard> mapToLostPostCards(Page<LostPost> lostPostPage, Clock clock) {
        return lostPostPage.getContent().stream()
                .map(lp -> {
                    String author = userInfoPort.getNicknameByUserId(lp.getUserId());
                    return LostPostCardMapper.toLostPostCard(lp, author, clock);
                })
                .collect(Collectors.toList());
    }
}

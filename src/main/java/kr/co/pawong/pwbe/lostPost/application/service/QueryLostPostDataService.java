package kr.co.pawong.pwbe.lostPost.application.service;

import java.time.Clock;
import java.util.List;
import java.util.stream.Collectors;
import kr.co.pawong.pwbe.lostPost.application.port.in.QueryLostPostDataUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostDetailDto;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostDetailResponse;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.SliceLostPostSearchResponses;
import kr.co.pawong.pwbe.lostPost.application.port.in.mapper.LostPostCardMapper;
import kr.co.pawong.pwbe.lostPost.application.port.in.mapper.LostPostDetailMapper;
import kr.co.pawong.pwbe.lostPost.application.port.out.BookmarkInfoPort;
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
    private final BookmarkInfoPort bookmarkInfoPort;
    private final Clock clock;

    @Override
    @Transactional(readOnly = true)
    public List<LostPostCard> getLostPostsByUserId(Long userId) {

        List<LostPost> lostPosts = lostPostDataQueryPort.getLostPostsByUserId(userId);
        String author = userInfoPort.getNicknameByUserId(userId);

        return lostPosts.stream()
                .map(post -> {
                    boolean bookmarked = bookmarkInfoPort.existsByUserIdAndLostPostId(userId, post.getLostPostId());
                    return LostPostCardMapper.toLostPostCard(post, author, bookmarked, clock);
                })
                .toList();
    }

    @Override
    public LostPostDetailResponse findLostPostById(Long lostPostId) {

        LostPost lostPost = lostPostDataQueryPort.findLostPostByIdOrThrow(lostPostId);
        String author = userInfoPort.getNicknameByUserId(lostPost.getUserId());
        boolean bookmarked = bookmarkInfoPort.existsByUserIdAndLostPostId(lostPost.getUserId(), lostPost.getLostPostId());
        LostPostDetailDto lostPostDetailDto = LostPostDetailMapper.toModel(lostPost, author, bookmarked, clock);

        return new LostPostDetailResponse(lostPostDetailDto);
    }

    @Override
    public SliceLostPostSearchResponses fetchSlicedLostPosts(Pageable pageable, PostType type, Long userId) {
        Page<LostPost> lostPostPage = lostPostDataQueryPort.getLostPostsByPostTypePaged(pageable, type);
        List<LostPostCard> lostPostCards = mapToLostPostCards(lostPostPage, clock, userId);
        boolean hasNext = lostPostPage.hasNext();
        return new SliceLostPostSearchResponses(hasNext,lostPostCards);
    }

    private List<LostPostCard> mapToLostPostCards(Page<LostPost> lostPostPage, Clock clock, Long userId) {
        return lostPostPage.getContent().stream()
                .map(lp -> {
                    String author = userInfoPort.getNicknameByUserId(lp.getUserId());
                    boolean bookmarked = bookmarkInfoPort.existsByUserIdAndLostPostId(userId, lp.getLostPostId());
                    return LostPostCardMapper.toLostPostCard(lp, author, bookmarked,clock);
                })
                .collect(Collectors.toList());
    }
}

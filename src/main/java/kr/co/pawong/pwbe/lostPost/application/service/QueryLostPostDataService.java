package kr.co.pawong.pwbe.lostPost.application.service;

import java.net.URL;
import java.time.Clock;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import kr.co.pawong.pwbe.infrastructure.s3.application.port.out.ImageStoragePort;
import kr.co.pawong.pwbe.lostPost.application.port.in.QueryLostPostDataUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostDetailDto;
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
    private final ImageStoragePort imageStoragePort;
    private final Clock clock;
    private static final Duration DOWNLOAD_URL_EXPIRE = Duration.ofMinutes(15);

    @Override
    @Transactional(readOnly = true)
    public List<LostPostCard> getLostPostsByUserId(Long userId) {

        List<LostPost> lostPosts = lostPostDataQueryPort.getLostPostsByUserId(userId);
        String author = userInfoPort.getNicknameByUserId(userId);

        return lostPosts.stream()
                .map(post -> {
                    boolean bookmarked = bookmarkInfoPort.existsByUserIdAndLostPostId(userId, post.getLostPostId());
                    String url = imageStoragePort.presignDownload(post.getImageKey(), DOWNLOAD_URL_EXPIRE).toString();
                    return LostPostCardMapper.toLostPostCard(post, author, bookmarked, clock, url);
                })
                .toList();
    }

    @Override
    public LostPostDetailDto findLostPostById(Long lostPostId) {

        LostPost lostPost = lostPostDataQueryPort.findLostPostByIdOrThrow(lostPostId);
        String author = userInfoPort.getNicknameByUserId(lostPost.getUserId());
        URL url = imageStoragePort.presignDownload(lostPost.getImageKey(), DOWNLOAD_URL_EXPIRE);
        boolean bookmarked = bookmarkInfoPort.existsByUserIdAndLostPostId(lostPost.getUserId(), lostPost.getLostPostId());

        return LostPostDetailMapper.toModel(lostPost, author, bookmarked, clock, url);
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
                    String url = imageStoragePort.presignDownload(lp.getImageKey(), DOWNLOAD_URL_EXPIRE).toString();
                    boolean bookmarked = bookmarkInfoPort.existsByUserIdAndLostPostId(userId, lp.getLostPostId());
                    return LostPostCardMapper.toLostPostCard(lp, author, bookmarked,clock,url);
                })
                .collect(Collectors.toList());
    }
}

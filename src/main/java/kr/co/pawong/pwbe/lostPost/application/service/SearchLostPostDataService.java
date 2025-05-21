package kr.co.pawong.pwbe.lostPost.application.service;

import java.time.Clock;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import kr.co.pawong.pwbe.infrastructure.s3.application.port.out.ImageStoragePort;
import kr.co.pawong.pwbe.lostPost.adapter.in.api.dto.request.LostPostSearchRequest;
import kr.co.pawong.pwbe.lostPost.application.port.in.SearchLostPostDataUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostSearchResponses;
import kr.co.pawong.pwbe.lostPost.application.port.in.mapper.LostPostCardMapper;
import kr.co.pawong.pwbe.lostPost.application.port.out.BookmarkInfoPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostPostDataQueryPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.UserInfoPort;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchLostPostDataService implements SearchLostPostDataUseCase {

    private final LostPostDataQueryPort lostPostDataQueryPort;
    private final UserInfoPort userInfoPort;
    private final BookmarkInfoPort bookmarkInfoPort;
    private final ImageStoragePort imageStoragePort;
    private final Clock clock;
    private static final Duration DOWNLOAD_URL_EXPIRE = Duration.ofMinutes(15);

    @Override
    public LostPostSearchResponses searchLostPosts(LostPostSearchRequest request, Pageable pageable) {
        Page<LostPost> lostPostPage = lostPostDataQueryPort.searchLostPosts(pageable, request);
        List<LostPostCard> lostPostcards = mapToLostPostCards(lostPostPage,clock);
        boolean hasNext = lostPostPage.hasNext();
        return new LostPostSearchResponses(hasNext,lostPostcards);
    }

    private List<LostPostCard> mapToLostPostCards(Page<LostPost> lostPostPage, Clock clock) {
        return lostPostPage.getContent().stream()
                .map(lp -> {
                    String author = userInfoPort.getNicknameByUserId(lp.getUserId());
                    String url = imageStoragePort.presignDownload(lp.getImageKey(), DOWNLOAD_URL_EXPIRE).toString();
                    boolean bookmarked = bookmarkInfoPort.existsByUserIdAndLostPostId(lp.getUserId(), lp.getLostPostId());
                    return LostPostCardMapper.toLostPostCard(lp, author, bookmarked, clock, url);
                })
                .collect(Collectors.toList());
    }
}

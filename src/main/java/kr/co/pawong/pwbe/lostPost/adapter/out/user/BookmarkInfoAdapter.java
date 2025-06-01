package kr.co.pawong.pwbe.lostPost.adapter.out.user;

import kr.co.pawong.pwbe.lostPost.application.port.out.BookmarkInfoPort;
import kr.co.pawong.pwbe.user.application.port.in.QueryBookmarkDataUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookmarkInfoAdapter implements BookmarkInfoPort {

    private final QueryBookmarkDataUseCase queryBookmarkDataUseCase;

    @Override
    public boolean existsByUserIdAndLostPostId(Long userId, Long lostPostId) {
        return queryBookmarkDataUseCase.existsLostPostBookmark(userId, lostPostId);
    }

    @Override
    public boolean existsByUserIdAndAdoptionId(Long userId, Long adoptionId) {
        return queryBookmarkDataUseCase.existsLostAdoptionBookmark(userId, adoptionId);
    }

}

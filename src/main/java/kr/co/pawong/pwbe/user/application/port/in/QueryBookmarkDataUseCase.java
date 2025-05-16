package kr.co.pawong.pwbe.user.application.port.in;

import java.util.List;
import kr.co.pawong.pwbe.user.domain.LostBookmark;

public interface QueryBookmarkDataUseCase {

    List<LostBookmark> getBookmarksByUserId(long userId);

    boolean lostPostBookmarkExists(Long userId, long lostPostId);

    boolean lostAdoptionBookmarkExists(Long userId, long bookmarkId);

}

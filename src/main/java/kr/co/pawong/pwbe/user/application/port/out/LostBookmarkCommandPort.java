package kr.co.pawong.pwbe.user.application.port.out;

import kr.co.pawong.pwbe.user.domain.LostBookmark;

public interface LostBookmarkCommandPort {

    LostBookmark save(LostBookmark bookmark);

    void delete(Long bookmarkId);

    void deleteByLostPostId(Long lostPostId);
}

package kr.co.pawong.pwbe.user.application.port.in;

public interface ToggleBookmarkUseCase {

    /**
     * LostPost 북마크 토글
     */
    boolean toggleLostPostBookmark(Long userId, Long lostPostId);
}

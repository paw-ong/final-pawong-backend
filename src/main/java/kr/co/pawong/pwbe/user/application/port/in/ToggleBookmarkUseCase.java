package kr.co.pawong.pwbe.user.application.port.in;

public interface ToggleBookmarkUseCase {

    /**
     * LostPost 북마크 토글
     */
    boolean toggleLostPostBookmark(long userId, long lostPostId);

    /**
     * Adoption Lost 북마크 토글
     */
    boolean toggleLostAdoptionBookmark(long userId, long adoptionId);
}

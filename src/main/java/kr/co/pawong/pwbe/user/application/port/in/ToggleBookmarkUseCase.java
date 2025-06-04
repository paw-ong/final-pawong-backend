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

    // 게시글 삭제시 게시글에 포함된 북마크도 삭제
    void deleteByLostPostId(Long lostPostId);
}

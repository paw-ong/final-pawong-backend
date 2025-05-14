package kr.co.pawong.pwbe.user.application.service;

import kr.co.pawong.pwbe.user.application.port.in.ToggleBookmarkUseCase;
import kr.co.pawong.pwbe.user.application.port.out.LostBookmarkCommandPort;
import kr.co.pawong.pwbe.user.application.port.out.LostBookmarkQueryPort;
import kr.co.pawong.pwbe.user.domain.LostBookmark;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ToggleBookmarkService implements ToggleBookmarkUseCase {

    private final LostBookmarkQueryPort bookmarkQueryPort;
    private final LostBookmarkCommandPort bookmarkCommandPort;

    // 실종 게시물에 대한 Bookmark
    @Override
    @Transactional
    public boolean toggleLostPostBookmark(long userId, long lostPostId) {

        return bookmarkQueryPort.findByLostPostId(userId, lostPostId)
                .map(bookmark -> {
                    // 있으면 북마크 삭제
                    bookmarkCommandPort.delete(bookmark.getBookmarkId());
                    return false;
                })
                .orElseGet(() -> {
                    // 없으면 북마크 생성
                    bookmarkCommandPort.save(
                            LostBookmark.createByLostPostId(userId, lostPostId)
                    );
                    return true;
                });
    }

    // 실종 API 데이터에 대한 Bookmark
    @Override
    @Transactional
    public boolean toggleLostAdoptionBookmark(long userId, long adoptionId) {
        return bookmarkQueryPort.findByAdoptionId(userId, adoptionId)
                .map(bookmark -> {
                    // 있으면 북마크 삭제
                    bookmarkCommandPort.delete(bookmark.getBookmarkId());
                    return false;
                })
                .orElseGet(() -> {
                    // 없으면 북마크 생성
                    bookmarkCommandPort.save(
                            LostBookmark.createByAdoptionId(userId, adoptionId)
                    );
                    return true;
                });
    }
}

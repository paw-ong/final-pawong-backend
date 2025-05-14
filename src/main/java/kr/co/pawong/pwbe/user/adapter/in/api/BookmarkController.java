package kr.co.pawong.pwbe.user.adapter.in.api;

import kr.co.pawong.pwbe.user.adapter.in.api.dto.response.BookmarkResponse;
import kr.co.pawong.pwbe.user.adapter.out.security.CustomUserDetails;
import kr.co.pawong.pwbe.user.application.port.in.ToggleBookmarkUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/users/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final ToggleBookmarkUseCase toggleBookmarkUseCase;

    @PostMapping("/lost-posts/{lostPostId}/toggle")
    public ResponseEntity<BookmarkResponse> toggleLostPostBookmark(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long lostPostId
    ) {
        // 북마크 토글 후 북마크 상태 받아오기
        boolean bookmarked = toggleBookmarkUseCase.toggleLostPostBookmark(principal.getUserId(), lostPostId);
        return ResponseEntity.ok(new BookmarkResponse(bookmarked));
    }

    @PostMapping("/adoptions/{adoptionId}/toggle")
    public ResponseEntity<BookmarkResponse> toggleAdoptionPostBookmark(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long adoptionId
    ) {
        // 북마크 토글 후 북마크 상태 받아오기
        boolean bookmarked = toggleBookmarkUseCase.toggleLostAdoptionBookmark(principal.getUserId(), adoptionId);
        return ResponseEntity.ok(new BookmarkResponse(bookmarked));
    }
}

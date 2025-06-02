package kr.co.pawong.pwbe.user.adapter.in.api;

import kr.co.pawong.pwbe.user.adapter.in.api.dto.response.BookmarkResponse;
import kr.co.pawong.pwbe.user.application.port.in.QueryBookmarkDataUseCase;
import kr.co.pawong.pwbe.global.security.dto.CustomUserDetails;
import kr.co.pawong.pwbe.user.application.port.in.ToggleBookmarkUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/users/bookmarks/lost-animals")
@RequiredArgsConstructor
public class LostBookmarkController {

    private final ToggleBookmarkUseCase toggleBookmarkUseCase;
    private final QueryBookmarkDataUseCase queryBookmarkDataUseCase;

    @PostMapping("/lost-posts/{lostPostId}/toggle")
    public ResponseEntity<BookmarkResponse> toggleLostPostsBookmark(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long lostPostId
    ) {
        // 북마크 토글 후 북마크 상태 받아오기
        boolean bookmarked = toggleBookmarkUseCase.toggleLostPostBookmark(principal.getUserId(),
                lostPostId);
        return ResponseEntity.ok(new BookmarkResponse(bookmarked));
    }

    @PostMapping("/lost-adoptions/{lostAdoptionId}/toggle")
    public ResponseEntity<BookmarkResponse> toggleLostAdoptionBookmark(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long lostAdoptionId
    ) {
        // 북마크 토글 후 북마크 상태 받아오기
        boolean bookmarked = toggleBookmarkUseCase.toggleLostAdoptionBookmark(principal.getUserId(),
                lostAdoptionId);
        return ResponseEntity.ok(new BookmarkResponse(bookmarked));
    }

    @GetMapping("/lost-posts/{lostPostId}/status")
    public ResponseEntity<BookmarkResponse> checkLostPostBookmarkStatus(
            @PathVariable Long lostPostId,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        // true: 이미 북마크한 상태, false: 북마크하지 않은 상태
        boolean existsBookmark = queryBookmarkDataUseCase.existsLostPostBookmark(principal.getUserId(),
                lostPostId);
        return ResponseEntity.ok(new BookmarkResponse(existsBookmark));
    }

    @GetMapping("/lost-adoptions/{lostAdoptionId}/status")
    public ResponseEntity<BookmarkResponse> checkLostAdoptionBookmarkStatus(
            @PathVariable Long lostAdoptionId,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        // true: 이미 북마크한 상태, false: 북마크하지 않은 상태
        boolean existsBookmark = queryBookmarkDataUseCase.existsLostAdoptionBookmark(principal.getUserId(),
                lostAdoptionId);
        return ResponseEntity.ok(new BookmarkResponse(existsBookmark));
    }

}

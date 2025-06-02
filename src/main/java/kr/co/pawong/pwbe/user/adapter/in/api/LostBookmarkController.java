package kr.co.pawong.pwbe.user.adapter.in.api;

import kr.co.pawong.pwbe.user.adapter.in.api.dto.response.BookmarkResponse;
import kr.co.pawong.pwbe.global.security.dto.CustomUserDetails;
import kr.co.pawong.pwbe.user.application.port.in.ToggleBookmarkUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping("/lost-posts/{id}/toggle")
    public ResponseEntity<BookmarkResponse> toggleLostPostsBookmark(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long id
    ) {
        // 북마크 토글 후 북마크 상태 받아오기
        boolean bookmarked = toggleBookmarkUseCase.toggleLostPostBookmark(principal.getUserId(),
                id);
        return ResponseEntity.ok(new BookmarkResponse(bookmarked));
    }

    @PostMapping("/lost-adoptions/{id}/toggle")
    public ResponseEntity<BookmarkResponse> toggleLostAdoptionBookmark(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long id
    ) {
        // 북마크 토글 후 북마크 상태 받아오기
        boolean bookmarked = toggleBookmarkUseCase.toggleLostAdoptionBookmark(principal.getUserId(),
                id);
        return ResponseEntity.ok(new BookmarkResponse(bookmarked));
    }

}

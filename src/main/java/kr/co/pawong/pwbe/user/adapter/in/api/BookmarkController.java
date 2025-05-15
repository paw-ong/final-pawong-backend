package kr.co.pawong.pwbe.user.adapter.in.api;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.LOST_ANIMAL_TYPE_MISSING_ERROR;

import kr.co.pawong.pwbe.global.error.exception.BaseException;
import kr.co.pawong.pwbe.user.adapter.in.api.dto.response.BookmarkResponse;
import kr.co.pawong.pwbe.user.adapter.out.security.CustomUserDetails;
import kr.co.pawong.pwbe.user.application.port.in.ToggleBookmarkUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/users/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final ToggleBookmarkUseCase toggleBookmarkUseCase;

    @PostMapping("/lost-animals/{id}/toggle")
    public ResponseEntity<BookmarkResponse> toggleLostPostBookmark(
            @RequestParam(required = false) String type,
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long id
    ) {
        boolean bookmarked;
        // 북마크 토글 후 북마크 상태 받아오기
        if (type.equals("lost-posts")) {
            bookmarked = toggleBookmarkUseCase.toggleLostPostBookmark(principal.getUserId(), id);
        } else if (type.equals("lost-adoptions")) {
            bookmarked = toggleBookmarkUseCase.toggleLostAdoptionBookmark(principal.getUserId(), id);
        } else {
            throw new BaseException(LOST_ANIMAL_TYPE_MISSING_ERROR);
        }
        return ResponseEntity.ok(new BookmarkResponse(bookmarked));
    }

}

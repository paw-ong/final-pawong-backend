package kr.co.pawong.pwbe.user.adapter.in.api;

import java.util.List;
import kr.co.pawong.pwbe.user.adapter.in.api.dto.response.BaseMyPageResponse;
import kr.co.pawong.pwbe.user.adapter.out.security.CustomUserDetails;
import kr.co.pawong.pwbe.user.application.port.in.QueryMyPageDataUseCase;
import kr.co.pawong.pwbe.user.application.port.in.dto.MyPageFavoritesResponse;
import kr.co.pawong.pwbe.user.application.port.in.dto.MyPageLostPostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/me")
@RequiredArgsConstructor
public class MyPageController {

    private final QueryMyPageDataUseCase queryMyPageDataUseCase;

    @GetMapping("/lost-posts")
    public ResponseEntity<BaseMyPageResponse<MyPageLostPostResponse>> myPageLostPosts(
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        List<MyPageLostPostResponse> content = queryMyPageDataUseCase.getLostPostsByUserId(
                principal.getUserId());
        return ResponseEntity.ok(new BaseMyPageResponse<>(content));
    }

    @GetMapping("/lost-bookmarks")
    public ResponseEntity<BaseMyPageResponse<MyPageLostPostResponse>> myPageLostBookmarks(
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        List<MyPageLostPostResponse> content = queryMyPageDataUseCase.getBookmarkedLostPostsByUserId(
                principal.getUserId());
        return ResponseEntity.ok(new BaseMyPageResponse<>(content));
    }

    @GetMapping("/favorites")
    public ResponseEntity<BaseMyPageResponse<MyPageFavoritesResponse>> getFavorites(
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        List<MyPageFavoritesResponse> content = queryMyPageDataUseCase.getFavoritesListByUserId(
                principal.getUserId());
        return ResponseEntity.ok(
                new BaseMyPageResponse<>(content)
        );
    }
}

package kr.co.pawong.pwbe.favorites.presentation;

import kr.co.pawong.pwbe.favorites.application.service.FavoritesService;
import kr.co.pawong.pwbe.favorites.application.service.dto.FavoritesRequest;
import kr.co.pawong.pwbe.favorites.presentation.dto.response.FavoritesListResponse;
import kr.co.pawong.pwbe.favorites.presentation.dto.response.FavoritesResponse;
import kr.co.pawong.pwbe.user.adapter.out.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users/favorites")
@RequiredArgsConstructor
public class FavoritesController {

    private final FavoritesService favoritesService;

    /**
     * 현재 로그인된 유저가 adoption 공고를 토글 방식으로 찜하는 API
     */
    @PostMapping("/{adoptionId}/toggle")
    public ResponseEntity<FavoritesResponse> toggleFavorite(
            @PathVariable Long adoptionId,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        Long userId = principal.getUserId();
        FavoritesRequest request = FavoritesRequest.builder()
                .userId(userId)
                .adoptionId(adoptionId)
                .build();

        // 호출 결과로 찜 완료: true, 찜 취소: false
        FavoritesResponse response = favoritesService.toggleFavorite(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 현재 로그인된 유저가 adoptionId 공고를 찜했는지 상태를 확인하는 API
     */
    @GetMapping("/{adoptionId}/status")
    public ResponseEntity<FavoritesResponse> checkFavoriteStatus(
            @PathVariable Long adoptionId,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        Long userId = principal.getUserId();
        FavoritesRequest request = FavoritesRequest.builder()
                .userId(userId)
                .adoptionId(adoptionId)
                .build();

        // true: 이미 찜한 상태, false: 찜하지 않은 상태
        FavoritesResponse response = favoritesService.checkFavoriteStatus(request);
        return ResponseEntity.ok(response);
    }
}
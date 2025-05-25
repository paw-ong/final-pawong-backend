package kr.co.pawong.pwbe.user.adapter.in.api;

import kr.co.pawong.pwbe.user.adapter.in.api.dto.response.FavoritesResponse;
import kr.co.pawong.pwbe.user.adapter.out.security.CustomUserDetails;
import kr.co.pawong.pwbe.user.application.port.in.QueryFavoritesDataUseCase;
import kr.co.pawong.pwbe.user.application.port.in.ToggleFavoritesUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/favorites")
@RequiredArgsConstructor
public class FavoritesController {

    private final ToggleFavoritesUseCase toggleFavoritesUseCase;
    private final QueryFavoritesDataUseCase queryFavoritesDataUseCase;

    @PostMapping("/{adoptionId}/toggle")
    public ResponseEntity<FavoritesResponse> toggleFavorite(
            @PathVariable Long adoptionId,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        boolean isInFavorites = toggleFavoritesUseCase.toggleFavorites(principal.getUserId(),
                adoptionId);
        return ResponseEntity.ok(new FavoritesResponse(isInFavorites));
    }

    // 유기동물 카드 등장 시마다 각 카드에 대한 찜 여부를 확인하는 api endpoint
    @GetMapping("/{adoptionId}/status")
    public ResponseEntity<FavoritesResponse> checkFavoriteStatus(
            @PathVariable Long adoptionId,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        // true: 이미 찜한 상태, false: 찜하지 않은 상태
        boolean inFavorites = queryFavoritesDataUseCase.isInFavorites(principal.getUserId(),
                adoptionId);
        return ResponseEntity.ok(new FavoritesResponse(inFavorites));
    }
}

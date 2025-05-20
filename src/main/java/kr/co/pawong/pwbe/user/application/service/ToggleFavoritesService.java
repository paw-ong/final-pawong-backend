package kr.co.pawong.pwbe.user.application.service;

import kr.co.pawong.pwbe.user.domain.Favorites;
import kr.co.pawong.pwbe.user.application.port.in.ToggleFavoritesUseCase;
import kr.co.pawong.pwbe.user.application.port.out.FavoritesCommandPort;
import kr.co.pawong.pwbe.user.application.port.out.FavoritesQueryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ToggleFavoritesService implements ToggleFavoritesUseCase {

    private final FavoritesQueryPort favoritesQueryPort;
    private final FavoritesCommandPort favoritesCommandPort;

    @Override
    public boolean toggleFavorites(Long userId, Long adoptionId) {
        return favoritesQueryPort
                .findByUserIdAndAdoptionId(userId, adoptionId)
                .map(fav -> {
                    // 이미 찜되어 있으면 삭제(취소)
                    favoritesCommandPort.deleteById(fav.getFavoritesId());
                    log.info("찜 취소. (userId={}, adoptionId={})",
                            userId, adoptionId);
                    return false;
                })
                .orElseGet(() -> {
                    // 아직 찜하지 않았으면 새로 저장
                    Favorites newFav = Favorites.of(userId, adoptionId);
                    favoritesCommandPort.save(newFav);
                    log.info("찜 추가. (userId={}, adoptionId={})", userId, adoptionId);
                    return true;
                });
    }
}

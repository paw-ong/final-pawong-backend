package kr.co.pawong.pwbe.user.application.port.in;

public interface ToggleFavoritesUseCase {

    boolean toggleFavorites(Long userId, Long adoptionId);
}

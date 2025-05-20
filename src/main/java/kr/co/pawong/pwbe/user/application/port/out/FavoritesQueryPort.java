package kr.co.pawong.pwbe.user.application.port.out;

import java.util.List;
import java.util.Optional;
import kr.co.pawong.pwbe.user.domain.Favorites;

public interface FavoritesQueryPort {

    Optional<Favorites> findByUserIdAndAdoptionId(Long userId, Long adoptionId);

    List<Favorites> findAllByUserId(Long userId);

    boolean favoritesExist(Long userId, Long adoptionId);

}

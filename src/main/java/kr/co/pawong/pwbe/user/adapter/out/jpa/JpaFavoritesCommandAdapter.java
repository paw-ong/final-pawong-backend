package kr.co.pawong.pwbe.user.adapter.out.jpa;

import kr.co.pawong.pwbe.user.adapter.out.jpa.entity.FavoritesEntity;
import kr.co.pawong.pwbe.user.adapter.out.jpa.repository.FavoritesJpaRepository;
import kr.co.pawong.pwbe.user.application.port.out.FavoritesCommandPort;
import kr.co.pawong.pwbe.user.domain.Favorites;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaFavoritesCommandAdapter implements FavoritesCommandPort {

    private final FavoritesJpaRepository favoritesRepository;

    @Override
    public Favorites save(Favorites favorites) {
        return favoritesRepository.save(FavoritesEntity.of(favorites)).toDomain();
    }

    @Override
    public void deleteById(Long favoritesId) {
        favoritesRepository.deleteById(favoritesId);
    }
}

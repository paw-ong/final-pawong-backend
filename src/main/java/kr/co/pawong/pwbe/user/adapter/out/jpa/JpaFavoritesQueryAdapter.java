package kr.co.pawong.pwbe.user.adapter.out.jpa;

import java.util.List;
import java.util.Optional;
import kr.co.pawong.pwbe.user.adapter.out.jpa.entity.FavoritesEntity;
import kr.co.pawong.pwbe.user.adapter.out.jpa.repository.FavoritesJpaRepository;
import kr.co.pawong.pwbe.user.application.port.out.FavoritesQueryPort;
import kr.co.pawong.pwbe.user.domain.Favorites;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaFavoritesQueryAdapter implements FavoritesQueryPort {

    private final FavoritesJpaRepository favoritesRepository;

    @Override
    public Optional<Favorites> findByUserIdAndAdoptionId(Long userId, Long adoptionId) {
        return favoritesRepository.findFavoritesEntityByUserIdAndAdoptionId(userId, adoptionId)
                .map(FavoritesEntity::toDomain);
    }

    @Override
    public List<Favorites> findAllByUserId(Long userId) {
        return favoritesRepository.findAllByUserId(userId).stream()
                .map(FavoritesEntity::toDomain)
                .toList();
    }

    @Override
    public boolean favoritesExist(Long userId, Long adoptionId) {
        return favoritesRepository.existsByUserIdAndAdoptionId(userId, adoptionId);
    }
}
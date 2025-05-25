package kr.co.pawong.pwbe.user.adapter.out.jpa.repository;

import java.util.List;
import java.util.Optional;
import kr.co.pawong.pwbe.user.adapter.out.jpa.entity.FavoritesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritesJpaRepository extends JpaRepository<FavoritesEntity, Long> {

    Optional<FavoritesEntity> findFavoritesEntityByUserIdAndAdoptionId(Long userId,
            Long adoptionId);

    List<FavoritesEntity> findAllByUserId(Long userId);

    boolean existsByUserIdAndAdoptionId(Long userId, Long adoptionId);

}

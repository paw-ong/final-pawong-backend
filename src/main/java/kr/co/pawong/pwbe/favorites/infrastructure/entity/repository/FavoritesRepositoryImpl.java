package kr.co.pawong.pwbe.favorites.infrastructure.entity.repository;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.ADOPTION_NOT_FOUND;
import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.USER_NOT_FOUND;

import kr.co.pawong.pwbe.adoption.adapter.out.persistence.jpa.repository.AdoptionJpaRepository;
import kr.co.pawong.pwbe.adoption.adapter.out.persistence.jpa.entity.AdoptionEntity;
import kr.co.pawong.pwbe.favorites.application.domain.Favorites;
import kr.co.pawong.pwbe.favorites.application.service.port.FavoritesRepository;
import kr.co.pawong.pwbe.favorites.infrastructure.entity.FavoritesEntity;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import kr.co.pawong.pwbe.user.infrastructure.repository.UserJpaRepository;
import kr.co.pawong.pwbe.user.infrastructure.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FavoritesRepositoryImpl implements FavoritesRepository {

    private final FavoritesJpaRepository favoritesJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final AdoptionJpaRepository adoptionJpaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Favorites> findAllByUserId(Long userId) {
        return favoritesJpaRepository
                .findAllByUser_UserId(userId)
                .stream()
                .map(FavoritesEntity::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Favorites> findByUserIdAndAdoptionId(Long userId, Long adoptionId) {
        return favoritesJpaRepository
                .findByUser_UserIdAndAdoption_AdoptionId(userId, adoptionId)
                .map(FavoritesEntity::toDomain);
    }

    @Override
    @Transactional
    public void save(Favorites favorites) {
        UserEntity userEntity = userJpaRepository.findById(favorites.getUserId())
                .orElseThrow(() ->
                        new BaseException(USER_NOT_FOUND));
        AdoptionEntity adoptionEntity = adoptionJpaRepository.findById(favorites.getAdoptionId())
                .orElseThrow(() ->
                        new BaseException(ADOPTION_NOT_FOUND));

        FavoritesEntity entity = FavoritesEntity.from(favorites, userEntity, adoptionEntity);
        favoritesJpaRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        favoritesJpaRepository.deleteById(id);
    }
}
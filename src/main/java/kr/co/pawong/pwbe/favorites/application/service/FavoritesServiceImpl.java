package kr.co.pawong.pwbe.favorites.application.service;

import kr.co.pawong.pwbe.adoption.domain.model.Adoption;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionCard;
import kr.co.pawong.pwbe.adoption.application.port.out.AdoptionDataQueryPort;
import kr.co.pawong.pwbe.adoption.application.service.support.AdoptionCardMapper;
import kr.co.pawong.pwbe.favorites.application.domain.Favorites;
import kr.co.pawong.pwbe.favorites.application.service.dto.FavoritesRequest;
import kr.co.pawong.pwbe.favorites.application.service.port.FavoritesRepository;
import kr.co.pawong.pwbe.favorites.presentation.dto.response.FavoritesListResponse;
import kr.co.pawong.pwbe.favorites.presentation.dto.response.FavoritesResponse;
import kr.co.pawong.pwbe.user.application.port.out.UserDataQueryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoritesServiceImpl implements FavoritesService {

    private final FavoritesRepository favoritesRepository;
    private final UserDataQueryPort userDataQueryPort;
    private final AdoptionDataQueryPort adoptionDataQueryPort;

    // toggle 방식의 찜 (이미 찜을 한 경우 찜 취소. 찜을 안한 경우 찜.)
    @Override
    @Transactional
    public FavoritesResponse toggleFavorite(FavoritesRequest request) {
        final Long userId = request.getUserId();
        final Long adoptionId = request.getAdoptionId();

        // 1) User 존재 검증 (없으면 IllegalArgumentException)
        userDataQueryPort.findByUserIdOrThrow(userId);

        // 2) Adoption 존재 검증 (없으면 EntityNotFoundException 등)
        adoptionDataQueryPort.findByIdOrThrow(adoptionId);

        // 3) 토글 로직
        return favoritesRepository
                .findByUserIdAndAdoptionId(userId, adoptionId)
                .map(fav -> {
                    // 이미 찜되어 있으면 삭제(취소)
                    favoritesRepository.deleteById(fav.getFavoritesId());
                    log.info("찜 취소. (userId={}, adoptionId={})",
                            userId, adoptionId);
                    return FavoritesResponse.builder().isInFavorites(false).build();
                })
                .orElseGet(() -> {
                    // 아직 찜하지 않았으면 새로 저장
                    Favorites newFav = Favorites.of(userId, adoptionId);
                    favoritesRepository.save(newFav);
                    log.info("찜 추가. (userId={} adoptionId={})", userId, adoptionId);
                    return FavoritesResponse.builder().isInFavorites(true).build();
                });
    }

    // 찜목록 확인 기능
    @Override
    @Transactional(readOnly = true)
    public FavoritesListResponse findAllByUserId(Long userId) {
        userDataQueryPort.findByUserIdOrThrow(userId);   // 유저가 존재하는지 먼저 검증
        List<Favorites> favoritesList = favoritesRepository.findAllByUserId(userId);
        List<AdoptionCard> adoptionCards = mapFavoritesListToAdoptionCards(favoritesList);
        return FavoritesListResponse.builder()
                .favoritesList(adoptionCards)
                .build();
    }

    // 찜 여부를 확인하는 기능
    @Override
    @Transactional(readOnly = true)
    public FavoritesResponse checkFavoriteStatus(FavoritesRequest request) {
        Long userId = request.getUserId();
        Long adoptionId = request.getAdoptionId();

        // 유저와 공고 존재 검증
        userDataQueryPort.findByUserIdOrThrow(userId);
        adoptionDataQueryPort.findByIdOrThrow(adoptionId);

        // 사용자가 해당 공고를 찜했는지 확인 (존재하면 true, 없으면 false)
        boolean present = favoritesRepository
                .findByUserIdAndAdoptionId(userId, adoptionId)
                .isPresent();
        return FavoritesResponse.builder().isInFavorites(present).build();
    }

    private List<AdoptionCard> mapFavoritesListToAdoptionCards(List<Favorites> favoritesList) {
        List<AdoptionCard> adoptionCards = new ArrayList<>(favoritesList.size());
        for(Favorites favorites : favoritesList) {
            Long adoptionId = favorites.getAdoptionId();
            Adoption adoption = adoptionDataQueryPort.findByIdOrThrow(adoptionId);
            adoptionCards.add(AdoptionCardMapper.toAdoptionCard(adoption));
        }
        return adoptionCards;
    }
}
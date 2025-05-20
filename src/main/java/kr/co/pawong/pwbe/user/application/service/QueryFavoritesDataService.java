package kr.co.pawong.pwbe.user.application.service;

import java.util.ArrayList;
import java.util.List;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionCard;
import kr.co.pawong.pwbe.adoption.application.service.support.AdoptionCardMapper;
import kr.co.pawong.pwbe.adoption.domain.model.Adoption;
import kr.co.pawong.pwbe.user.domain.Favorites;
import kr.co.pawong.pwbe.user.application.port.in.QueryFavoritesDataUseCase;
import kr.co.pawong.pwbe.user.application.port.out.AdoptionInfoPort;
import kr.co.pawong.pwbe.user.application.port.out.FavoritesQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueryFavoritesDataService implements QueryFavoritesDataUseCase {

    private final FavoritesQueryPort favoritesQueryPort;
    private final AdoptionInfoPort adoptionInfoPort;  // mapping 메소드때문에 갖고온거임

    // 사용자가 해당 공고를 찜했는지 확인 (존재하면 true, 없으면 false)
    @Override
    public boolean isInFavorites(Long userId, Long adoptionId) {
        return favoritesQueryPort.favoritesExist(userId, adoptionId);
    }

    // 기존 FavoritesServiceImpl에서 갖고온 mapping 메소드. 여기서 활용 할지말지는 미정
    @Override
    public List<AdoptionCard> getAllFavoritesAdoptionCardsByUserId(Long userId) {
        List<Favorites> favoritesList = getAllFavoritesByUserId(userId);
        List<AdoptionCard> adoptionCards = new ArrayList<>(favoritesList.size());
        for(Favorites favorites : favoritesList) {
            Adoption adoption = adoptionInfoPort.findByIdOrThrow(favorites.getAdoptionId());
            adoptionCards.add(AdoptionCardMapper.toAdoptionCard(adoption));
        }
        return adoptionCards;
    }

    private List<Favorites> getAllFavoritesByUserId(Long userId) {
        return favoritesQueryPort.findAllByUserId(userId);
    }
}

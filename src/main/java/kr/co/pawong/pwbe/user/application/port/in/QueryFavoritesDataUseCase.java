package kr.co.pawong.pwbe.user.application.port.in;

import java.util.List;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionCard;

public interface QueryFavoritesDataUseCase {

    boolean isInFavorites(Long userId, Long adoptionid);

    List<AdoptionCard> getAllFavoritesAdoptionCardsByUserId(Long userId);

}

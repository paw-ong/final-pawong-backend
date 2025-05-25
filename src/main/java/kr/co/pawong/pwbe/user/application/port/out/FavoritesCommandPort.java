package kr.co.pawong.pwbe.user.application.port.out;

import kr.co.pawong.pwbe.user.domain.Favorites;

public interface FavoritesCommandPort {

    Favorites save(Favorites favorites);

    void deleteById(Long favoritesId);

}

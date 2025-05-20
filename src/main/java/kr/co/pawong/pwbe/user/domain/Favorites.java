package kr.co.pawong.pwbe.user.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@Builder
public class Favorites {

    private final Long favoritesId;     // 찜 ID
    private final Long userId;           // 사용자 ID
    private final Long adoptionId;      // 유기동물 ID

    public static Favorites of(Long userId, Long adoptionId) {
        return Favorites.builder()
                .favoritesId(null)
                .userId(userId)
                .adoptionId(adoptionId)
                .build();
    }
}
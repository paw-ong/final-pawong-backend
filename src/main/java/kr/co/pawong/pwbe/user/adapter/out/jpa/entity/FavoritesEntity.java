package kr.co.pawong.pwbe.user.adapter.out.jpa.entity;

import jakarta.persistence.*;
import kr.co.pawong.pwbe.user.domain.Favorites;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "Favorites",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "adoption_id"})
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoritesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoritesId;

    private Long userId;

    private Long adoptionId;

    public static FavoritesEntity of(Favorites favorites) {
        return FavoritesEntity.builder()
                .favoritesId(favorites.getFavoritesId())
                .userId(favorites.getUserId())
                .adoptionId(favorites.getAdoptionId())
                .build();
    }

    public Favorites toDomain() {
        return Favorites.builder()
                .favoritesId(this.favoritesId)
                .userId(this.getUserId())
                .adoptionId(this.getAdoptionId())
                .build();
    }
}
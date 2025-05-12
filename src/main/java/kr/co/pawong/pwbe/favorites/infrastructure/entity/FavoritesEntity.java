package kr.co.pawong.pwbe.favorites.infrastructure.entity;

import jakarta.persistence.*;
import kr.co.pawong.pwbe.adoption.adapter.out.persistence.jpa.entity.AdoptionEntity;
import kr.co.pawong.pwbe.favorites.application.domain.Favorites;
import kr.co.pawong.pwbe.user.infrastructure.repository.entity.UserEntity;
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
    @Column(name = "favorites_id", nullable = false)
    private Long favoritesId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adoption_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private AdoptionEntity adoption;

    /**
     * 신규 생성용 팩토리 (favoritesId는 DB에서 할당)
     */
    public static FavoritesEntity of(UserEntity user, AdoptionEntity adoption) {
        return FavoritesEntity.builder()
                .user(user)
                .adoption(adoption)
                .build();
    }

    /**
     * 도메인 → 엔티티 매핑
     */
    public static FavoritesEntity from(Favorites domain, UserEntity user, AdoptionEntity adoption) {
        return FavoritesEntity.builder()
                .favoritesId(domain.getFavoritesId())
                .user(user)
                .adoption(adoption)
                .build();
    }

    /**
     * 엔티티 → 도메인 매핑
     */
    public Favorites toDomain() {
        return Favorites.builder()
                .favoritesId(this.favoritesId)
                .userId(this.user.getUserId())
                .adoptionId(this.adoption.getAdoptionId())
                .build();
    }
}
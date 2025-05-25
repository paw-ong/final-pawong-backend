package kr.co.pawong.pwbe.user.adapter.out.jpa.repository;

import java.util.List;
import java.util.Optional;
import kr.co.pawong.pwbe.user.adapter.out.jpa.entity.LostBookmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LostBookmarkRepository extends JpaRepository<LostBookmarkEntity, Long> {

    Optional<LostBookmarkEntity> findLostBookmarkEntityByUserIdAndLostPostId(Long userId,
            Long lostPostId);

    Optional<LostBookmarkEntity> findLostBookmarkEntityByUserIdAndAdoptionId(Long userId,
            Long adoptionId);

    List<LostBookmarkEntity> findByUserId(Long userId);

    boolean existsByUserIdAndLostPostId(Long userId, Long lostPostId);

    boolean existsByUserIdAndAdoptionId(Long userId, Long adoptionId);
}

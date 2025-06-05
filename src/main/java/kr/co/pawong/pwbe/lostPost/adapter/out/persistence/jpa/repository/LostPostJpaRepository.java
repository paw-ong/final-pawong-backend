package kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.repository;

import java.util.List;
import java.util.Optional;
import kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.entity.LostPostEntity;
import kr.co.pawong.pwbe.lostPost.enums.PostStatus;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LostPostJpaRepository extends JpaRepository<LostPostEntity, Long> {

    List<LostPostEntity> findByUserId(Long userId);

    Page<LostPostEntity> findByPostType(PostType type, Pageable pageable);

    Optional<LostPostEntity> findByLostPostIdAndStatus(Long postId, PostStatus postStatus);

    // soft delete 무관하게 모두 조회
    @Query(value = "SELECT * FROM lost_posts WHERE lost_post_id = :lostPostId", nativeQuery = true)
    Optional<LostPostEntity> findAnyByLostPostId(@Param("lostPostId") Long lostPostId);
}

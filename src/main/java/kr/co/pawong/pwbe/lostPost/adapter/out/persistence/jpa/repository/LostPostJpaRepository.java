package kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.repository;

import java.util.List;
import java.util.Optional;
import kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.entity.LostPostEntity;
import kr.co.pawong.pwbe.lostPost.enums.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LostPostJpaRepository extends JpaRepository<LostPostEntity, Long> {

    List<LostPostEntity> findByUserId(Long userId);

    Optional<LostPostEntity> findByLostPostIdAndStatus(Long postId, PostStatus postStatus);
}

package kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.repository;

import java.util.List;
import kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.entity.LostPostEntity;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LostPostJpaRepository extends JpaRepository<LostPostEntity, Long> {

    List<LostPostEntity> findByUserId(Long userId);

    Page<LostPostEntity> findByPostType(PostType type, Pageable pageable);
}

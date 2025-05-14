package kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.repository;

import java.util.List;
import kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.entity.LostPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LostPostJpaRepository extends JpaRepository<LostPostEntity, Long> {

    List<LostPostEntity> findByUserId(Long userId);
}

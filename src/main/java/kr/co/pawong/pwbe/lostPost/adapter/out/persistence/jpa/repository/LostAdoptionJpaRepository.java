package kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.repository;

import kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.entity.LostAdoptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LostAdoptionJpaRepository extends JpaRepository<LostAdoptionEntity, Long> {

}

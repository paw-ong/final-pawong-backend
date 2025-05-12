package kr.co.pawong.pwbe.lostAnimal.adapter.out.persistence.repository;

import kr.co.pawong.pwbe.lostAnimal.adapter.out.persistence.entity.LostPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LostPostJpaRepository extends JpaRepository<LostPostEntity, Long> {

}

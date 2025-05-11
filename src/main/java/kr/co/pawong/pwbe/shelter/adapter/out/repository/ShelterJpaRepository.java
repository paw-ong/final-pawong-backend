package kr.co.pawong.pwbe.shelter.adapter.out.repository;

import kr.co.pawong.pwbe.shelter.adapter.out.entity.ShelterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShelterJpaRepository extends JpaRepository<ShelterEntity, Long> {

    List<ShelterEntity> findByCareNm(String careNm);
//    List<ShelterEntity> findByCareRegNo(String careRegNo);

    /**
     * 모든 ShelterEntity의 careRegNo만 추출해서 리스트로 반환하는 메서드
     */
    @Query("SELECT s.careRegNo FROM ShelterEntity s")
    List<String> findAllCareRegNos();

    Optional<ShelterEntity> findByCareRegNo(String careRegNo);
}

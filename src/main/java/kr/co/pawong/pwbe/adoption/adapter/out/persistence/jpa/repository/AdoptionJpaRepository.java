package kr.co.pawong.pwbe.adoption.adapter.out.persistence.jpa.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import kr.co.pawong.pwbe.adoption.enums.ActiveState;
import kr.co.pawong.pwbe.adoption.adapter.out.persistence.jpa.entity.AdoptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdoptionJpaRepository extends JpaRepository<AdoptionEntity, Long> {

    /**
     * adoptionId에 해당하는 AdoptionEntity의 isEmbedded 값을 부분 업데이트합니다.
     *
     * @param adoptionId 업데이트할 엔티티의 id
     * @param isEmbedded 임베딩 완료 여부
     */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE AdoptionEntity a SET a.isEmbedded = :isEmbedded WHERE a.adoptionId IN :adoptionId")
    void updateIsEmbedded(
            @Param("adoptionId") Long adoptionId,
            @Param("isEmbedded") boolean isEmbedded);

    /**
     * adoptionId에 해당하는 AdoptionEntity의 refinedSpecialMark, tagsField, isAiProcessed만 부분 업데이트합니다.
     *
     * @param adoptionId  업데이트할 엔티티의 id
     * @param refinedSpecialMark 정제된 필드 값
     * @param tagsField   정제된 태그 필드 값
     * @param isAiProcessed AI 정제 완료 여부
     */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE AdoptionEntity a SET a.refinedSpecialMark = :refinedSpecialMark, a.tagsField = :tagsField, a.isAiProcessed = :isAiProcessed WHERE a.adoptionId = :adoptionId")
    void updateAiFields(
            @Param("adoptionId") Long adoptionId,
            @Param("refinedSpecialMark") String refinedSpecialMark,
            @Param("tagsField") String tagsField,
            @Param("isAiProcessed") boolean isAiProcessed);

    @Query("SELECT a.careRegNo FROM AdoptionEntity a WHERE a.adoptionId = :id")
    String findCareRegNoByAdoptionId(@Param("id") Long id);

    // ActiveState = active, noticeEdt가 today와 같거나 가장 가까운 이후인 것
    List<AdoptionEntity> findTop12ByActiveStateAndNoticeEdtGreaterThanEqualOrderByNoticeEdtAsc(
            ActiveState activeState, LocalDate today);

    Optional<AdoptionEntity> findByAdoptionId(Long adoptionId);


}

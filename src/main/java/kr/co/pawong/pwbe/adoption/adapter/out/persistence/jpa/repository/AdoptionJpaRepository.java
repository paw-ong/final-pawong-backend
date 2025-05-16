package kr.co.pawong.pwbe.adoption.adapter.out.persistence.jpa.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kr.co.pawong.pwbe.adoption.adapter.out.persistence.jpa.entity.AdoptionEntity;
import kr.co.pawong.pwbe.adoption.enums.ActiveState;
import kr.co.pawong.pwbe.adoption.enums.NeuterYn;
import kr.co.pawong.pwbe.adoption.enums.ProcessState;
import kr.co.pawong.pwbe.adoption.enums.SexCd;
import kr.co.pawong.pwbe.adoption.enums.UpKindCd;
import kr.co.pawong.pwbe.adoption.enums.UpKindNm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdoptionJpaRepository extends JpaRepository<AdoptionEntity, Long> {

    // adoptionId에 해당하는 AdoptionEntity의 refinedSpecialMark, tagsField, isAiProcessed 업데이트
    @Modifying(clearAutomatically = true)
    @Query("UPDATE AdoptionEntity a SET a.refinedSpecialMark = :refinedSpecialMark, a.tagsField = :tagsField, a.isAiProcessed = :isAiProcessed WHERE a.adoptionId = :adoptionId")
    void updateAiFields(
            @Param("adoptionId") Long adoptionId,
            @Param("refinedSpecialMark") String refinedSpecialMark,
            @Param("tagsField") String tagsField,
            @Param("isAiProcessed") boolean isAiProcessed);

    @Query("SELECT a.careRegNo FROM AdoptionEntity a WHERE a.adoptionId = :id")
    String findCareRegNoByAdoptionId(@Param("id") Long id);

    // ActiveState = active, noticeEdt가 today와 같거나 가장 가까운 이후인 것 조회
    List<AdoptionEntity> findTop12ByActiveStateAndNoticeEdtGreaterThanEqualOrderByNoticeEdtAsc(
            ActiveState activeState, LocalDate today);

    Optional<AdoptionEntity> findByAdoptionId(Long adoptionId);

    List<AdoptionEntity> findAllByDesertionNo(String desertionNo);

    // 구조번호가 같고 updTm이 변경되었으면 업데이트
    @Modifying
    @Query("UPDATE AdoptionEntity a SET " +
            "a.happenDt = :happenDt, " + "a.happenPlace = :happenPlace, " +
            "a.upKindNm = :upKindNm, " + "a.upKindCd = :upKindCd, " +
            "a.kindNm = :kindNm, " + "a.kindCd = :kindCd, " +
            "a.colorCd = :colorCd, " + "a.age = :age, " +
            "a.weight = :weight, " + "a.noticeNo = :noticeNo, " +
            "a.noticeSdt = :noticeSdt, " + "a.noticeEdt = :noticeEdt, " +
            "a.popfile1 = :popfile1, " + "a.popfile2 = :popfile2, " +
            "a.processState = :processState, " + "a.activeState = :activeState, " +
            "a.sexCd = :sexCd, " + "a.neuterYn = :neuterYn, " +
            "a.specialMark = :specialMark, " + "a.careRegNo = :careRegNo, " +
            "a.updTm = :updTm, " + "a.refinedSpecialMark = null, " +
            "a.tagsField = null, " + "a.isAiProcessed = false, " +
            "a.isEmbedded = false " +
            "where a.desertionNo = :desertionNo and a.updTm < :updTm")
    void updateIfChanged(
            @Param("desertionNo") String desertionNo, @Param("happenDt") LocalDate happenDt,
            @Param("happenPlace") String happenPlace, @Param("upKindNm") UpKindNm upKindNm,
            @Param("upKindCd") UpKindCd upKindCd, @Param("kindNm") String kindNm,
            @Param("kindCd") String kindCd, @Param("colorCd") String colorCd,
            @Param("age") Integer age, @Param("weight") String weight,
            @Param("noticeNo") String noticeNo, @Param("noticeSdt") LocalDate noticeSdt,
            @Param("noticeEdt") LocalDate noticeEdt, @Param("popfile1") String popfile1,
            @Param("popfile2") String popfile2, @Param("processState") ProcessState processState,
            @Param("activeState") ActiveState activeState, @Param("sexCd") SexCd sexCd,
            @Param("neuterYn") NeuterYn neuterYn, @Param("specialMark") String specialMark,
            @Param("careRegNo") String careRegNo, @Param("updTm") LocalDateTime updTm
    );

    // id가 같으면 isEmbedded 업데이트
    @Modifying
    @Query("UPDATE AdoptionEntity a SET a.isEmbedded = true WHERE a.adoptionId IN :ids")
    void updateIsEmbeddedByIds(@Param("ids") List<Long> ids);

    // ActiveState가 MISSING, ADOPTED인 것과 IsProcessed가 False
    List<AdoptionEntity> findByActiveStateInAndAiProcessedFalse(List<ActiveState> activeStates);
}

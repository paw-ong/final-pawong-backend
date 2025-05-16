package kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import kr.co.pawong.pwbe.adoption.enums.ActiveState;
import kr.co.pawong.pwbe.adoption.enums.NeuterYn;
import kr.co.pawong.pwbe.adoption.enums.ProcessState;
import kr.co.pawong.pwbe.adoption.enums.SexCd;
import kr.co.pawong.pwbe.adoption.enums.UpKindCd;
import kr.co.pawong.pwbe.adoption.enums.UpKindNm;
import kr.co.pawong.pwbe.lostPost.domain.LostAdoption;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "Adoption")
// @Where은 추가 예정
public class LostAdoptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adoptionId; // 입양id

    private String desertionNo; // 구조번호

    private LocalDate happenDt; // 접수일

    private String happenPlace; // 발견장소

    @Enumerated(EnumType.STRING)
    private UpKindNm upKindNm; // 축종명

    @Enumerated(EnumType.STRING)
    private UpKindCd upKindCd; // 축종코드

    private String kindNm; // 품종명

    private String kindCd; // 품종코드

    private String colorCd; // 색상

    private Integer age; // 나이 - Long

    private String weight; // 체중

    private String noticeNo; // 공고번호

    private LocalDate noticeSdt; // 공고시작일

    private LocalDate noticeEdt; // 공고종료일

    @Column(columnDefinition = "TEXT")
    private String popfile1; // 이미지1(텍스트)

    @Column(columnDefinition = "TEXT")
    private String popfile2; // 이미지2(텍스트)

    @Enumerated(EnumType.STRING)
    private ProcessState processState; // 상태

    @Enumerated(EnumType.STRING)
    private ActiveState activeState;

    @Enumerated(EnumType.STRING)
    private SexCd sexCd; // 성별

    @Enumerated(EnumType.STRING)
    private NeuterYn neuterYn; // 중성화여부(타입)

    private String specialMark; // 특징

    private String careRegNo; // 보호소 번호

    private LocalDateTime updTm; // 수정일

    private String refinedSpecialMark; // 데이터 정제

    private String tagsField; // 태깅

    @Column(nullable = false)
    private boolean isAiProcessed;

    @Column(nullable = false)
    private boolean isEmbedded;

    public LostAdoption toDomain() {
        return LostAdoption.builder()
                .adoptionId(this.adoptionId)
                .desertionNo(this.desertionNo)
                .happenDt(this.happenDt)
                .happenPlace(this.happenPlace)
                .upKindCd(this.upKindCd)
                .upKindNm(this.upKindNm)
                .kindCd(this.kindCd)
                .kindNm(this.kindNm)
                .colorCd(this.colorCd)
                .age(this.age)
                .weight(this.weight)
                .noticeNo(this.noticeNo)
                .noticeSdt(this.noticeSdt)
                .noticeEdt(this.noticeEdt)
                .popfile1(this.popfile1)
                .popfile2(this.popfile2)
                .processState(this.processState)
                .activeState(this.activeState)
                .sexCd(this.sexCd)
                .neuterYn(this.neuterYn)
                .specialMark(this.specialMark)
                .careRegNo(this.careRegNo)
                .updTm(this.updTm)
                .refinedSpecialMark(this.refinedSpecialMark)
                .tagsField(this.tagsField)
                .isAiProcessed(this.isAiProcessed)
                .isEmbedded(this.isEmbedded)
                .build();
    }

}

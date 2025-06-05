package kr.co.pawong.pwbe.lostPost.application.port.in.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import kr.co.pawong.pwbe.adoption.enums.NeuterYn;
import kr.co.pawong.pwbe.adoption.enums.SexCd;
import kr.co.pawong.pwbe.adoption.enums.UpKindNm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LostAdoptionDetailDto {
    private Long adoptionId; // 입양id
    private LocalDate noticeSdt; // 공고시작일
    private LocalDate noticeEdt; // 공고종료일
    private LocalDateTime updTm; // 수정일
    private UpKindNm upKindNm; // 축종명
    private String kindNm; // 품종명
    private String colorCd; // 색상
    private SexCd sexCd; // 성별
    private Integer age; // 나이
    private String popfile1; // 이미지1(텍스트)
    private String popfile2; // 이미지2(텍스트)
    private String tagsField; // 특징
    private String happenPlace; // 발견장소
    private NeuterYn neuterYn; // 중성화여부(타입)
    private String careNm; // 보호소 번호

}

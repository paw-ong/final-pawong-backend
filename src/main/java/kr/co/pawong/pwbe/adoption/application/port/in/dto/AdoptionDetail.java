package kr.co.pawong.pwbe.adoption.application.port.in.dto;

import kr.co.pawong.pwbe.adoption.domain.model.Adoption;
import kr.co.pawong.pwbe.adoption.enums.NeuterYn;
import kr.co.pawong.pwbe.adoption.enums.SexCd;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdoptionDetail {
    private String careRegNo; // 보호소 번호
    private String kindNm; // 품종명
    private SexCd sexCd; // 성별
    private NeuterYn neuterYn; // 중성화여부(타입)
    private String weight; // 체중
    private Integer age; // 나이
    private String colorCd; // 색상
    private String desertionNo; // 구조번호
    private LocalDate noticeEdt; // 공고종료일
    private String tagsField; // 태깅
    private String popfile1; // 이미지1(텍스트)
    private String popfile2; // 이미지2(텍스트)

    public static AdoptionDetail from(Adoption adoption) {
        return AdoptionDetail.builder()
                .careRegNo(adoption.getCareRegNo())
                .kindNm(adoption.getKindNm())
                .sexCd(adoption.getSexCd())
                .neuterYn(adoption.getNeuterYn())
                .weight(adoption.getWeight())
                .age(adoption.getAge())
                .colorCd(adoption.getColorCd())
                .desertionNo(adoption.getDesertionNo())
                .noticeEdt(adoption.getNoticeEdt())
                .tagsField(adoption.getTagsField())
                .popfile1(adoption.getPopfile1())
                .popfile2(adoption.getPopfile2())
                .build();
    }

}

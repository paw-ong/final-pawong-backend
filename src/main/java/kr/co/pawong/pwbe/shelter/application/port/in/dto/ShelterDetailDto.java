package kr.co.pawong.pwbe.shelter.application.port.in.dto;

import kr.co.pawong.pwbe.shelter.domain.Shelter;
import kr.co.pawong.pwbe.shelter.enums.DivisionNm;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShelterDetailDto {
    private String careNm; // 동물보호센터명
    private String careTel; // 전화번호
    private String closeDay; // 휴무일
    private String saveTrgtAnimal; // 구조대상동물
    private DivisionNm divisionNm; // 동물보호센터 유형
    private String weekOprStime; // 평일 운영시작 시간
    private String weekOprEtime; // 평일 운영종료 시간
    private Integer vetPersonCnt; // 수의사 인원수
    private Integer specsPersonCnt; // 사양관리사 인원수
    private String careAddr; // 소재지 도로명 주소

    public static ShelterDetailDto from(Shelter shelter) {
        if (shelter == null) {
            // 보호소 정보가 없으면 null 반환
            return new ShelterDetailDto("","","","",null,"","",0,0,"");
        }
        return ShelterDetailDto.builder()
                .careNm(shelter.getCareNm())
                .careTel(shelter.getCareTel())
                .closeDay(shelter.getCloseDay())
                .saveTrgtAnimal(shelter.getSaveTrgtAnimal())
                .divisionNm(shelter.getDivisionNm())
                .weekOprStime(shelter.getWeekOprStime())
                .weekOprEtime(shelter.getWeekOprEtime())
                .vetPersonCnt(shelter.getVetPersonCnt())
                .specsPersonCnt(shelter.getSpecsPersonCnt())
                .careAddr(shelter.getCareAddr())
                .build();
    }

}

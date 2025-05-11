package kr.co.pawong.pwbe.adoption.application.port.in.dto;

import kr.co.pawong.pwbe.adoption.enums.NeuterYn;
import kr.co.pawong.pwbe.adoption.enums.SexCd;
import lombok.Builder;
import lombok.Getter;

// PetCard component에 노출되는 데이터를 담는 dto
@Getter
@Builder
public class AdoptionCard {
    private Long adoptionId;
    private String popfile1;
    private String kindNm;
    private SexCd sexCd;
    private Integer age;
    private NeuterYn neuterYn;
}

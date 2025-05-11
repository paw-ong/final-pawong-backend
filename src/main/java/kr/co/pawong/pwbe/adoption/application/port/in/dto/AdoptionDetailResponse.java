package kr.co.pawong.pwbe.adoption.application.port.in.dto;

import kr.co.pawong.pwbe.shelter.application.port.in.dto.ShelterDetailDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdoptionDetailResponse {
    private AdoptionDetail adoptionDetail;
    private ShelterDetailDto shelterDetailDto;
}

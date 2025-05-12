package kr.co.pawong.pwbe.shelter.application.port.in.dto;

import kr.co.pawong.pwbe.shelter.domain.Shelter;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShelterInfoDto {
    private String careRegNo; //동물보호센터번호
    private String city; // 시도
    private String district; // 시군구

    public static ShelterInfoDto from(Shelter shelter) {
        if (shelter == null) {
            // 보호소 정보가 없으면 null 반환
            return null;
        }
        return ShelterInfoDto.builder()
                .careRegNo(shelter.getCareRegNo())
                .city(shelter.getCity())
                .district(shelter.getDistrict())
                .build();
    }
}

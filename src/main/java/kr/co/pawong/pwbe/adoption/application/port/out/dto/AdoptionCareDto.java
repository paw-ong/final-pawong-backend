package kr.co.pawong.pwbe.adoption.application.port.out.dto;

import kr.co.pawong.pwbe.adoption.application.service.dto.AdoptionApi;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdoptionCareDto {
    private String careRegNo; // 보호소 번호
    private String careNm; // 보호소 이름
    private String careTel; // 보호소 전화번호
    private String careAddr; // 보호 장소
    private String orgNm;

    public static AdoptionCareDto from(AdoptionApi.Item item){
        return AdoptionCareDto.builder()
                .careRegNo(item.getCareRegNo())
                .careNm(item.getCareNm())
                .careTel(item.getCareTel())
                .careAddr(item.getCareAddr())
                .orgNm(item.getOrgNm())
                .build();
    }
}

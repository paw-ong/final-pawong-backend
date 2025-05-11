package kr.co.pawong.pwbe.adoption.application.port.in.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdoptionIdSearchResponse {
    private Long adoptionId;    // ES에서 검색 시 반환하는 것은 ID값
}

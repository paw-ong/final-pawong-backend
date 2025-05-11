package kr.co.pawong.pwbe.adoption.application.port.in.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

// ES에서 검색해오는 document들의 adoptionId 값들
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdoptionIdSearchResponses {
    private List<AdoptionIdSearchResponse> adoptionResponseList;
}

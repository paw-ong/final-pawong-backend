package kr.co.pawong.pwbe.lostPost.adapter.in.api.dto.response;

import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAdoptionDetailDto;
import kr.co.pawong.pwbe.shelter.application.port.in.dto.ShelterDetailDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LostAdoptionDetailResponse {
    private LostAdoptionDetailDto lostAdoptionDetailDto;
    private ShelterDetailDto shelterDetailDto;
}

package kr.co.pawong.pwbe.lostPost.adapter.in.api.dto.response;

import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAdoptionDetailDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LostAdoptionDetailResponse {
    private LostAdoptionDetailDto lostAdoptionDetailDto;
}

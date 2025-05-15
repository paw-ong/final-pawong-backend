package kr.co.pawong.pwbe.lostPost.application.port.in.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LostAdoptionDetailResponse {
    private LostAdoptionDetailDto lostAdoptionDetailDto;
}

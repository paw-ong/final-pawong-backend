package kr.co.pawong.pwbe.lostPost.adapter.in.api.dto.response;

import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostDetailDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LostPostDetailResponse {
    private LostPostDetailDto lostPostDetailDto;
}

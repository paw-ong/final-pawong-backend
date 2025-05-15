package kr.co.pawong.pwbe.lostPost.application.port.in.dto;

import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LostPostDetailResponse {
    private LostPostDetailDto lostPostDetailDto;
}

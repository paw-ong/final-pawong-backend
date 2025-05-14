package kr.co.pawong.pwbe.lostPost.application.port.in;

import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostDetailResponse;

public interface QueryLostPostDataUseCase {

    LostPostDetailResponse findLostPostById(Long lostPostId);

}

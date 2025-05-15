package kr.co.pawong.pwbe.lostPost.application.port.in;

import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAdoptionDetailDto;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAdoptionDetailResponse;

public interface QueryLostAdoptionDataUseCase {

    LostAdoptionDetailResponse findAdoptionById(Long adoptionId);

}

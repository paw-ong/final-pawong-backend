package kr.co.pawong.pwbe.lostPost.application.port.in;

import kr.co.pawong.pwbe.lostPost.adapter.in.api.dto.response.LostAdoptionDetailResponse;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAdoptionDetailDto;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.SliceLostPostSearchResponses;
import org.springframework.data.domain.Pageable;

public interface QueryLostAdoptionDataUseCase {

    SliceLostPostSearchResponses fetchSlicedLostAdoptions(Pageable pageable, Long userId);

    LostAdoptionDetailResponse getLostAdoptionDetails(Long adoptionId);
}

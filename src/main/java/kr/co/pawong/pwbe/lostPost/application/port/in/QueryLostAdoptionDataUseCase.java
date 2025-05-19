package kr.co.pawong.pwbe.lostPost.application.port.in;

import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAdoptionDetailDto;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAdoptionDetailResponse;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.SliceLostPostSearchResponses;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import org.springframework.data.domain.Pageable;

public interface QueryLostAdoptionDataUseCase {

    LostAdoptionDetailResponse findAdoptionById(Long adoptionId);

    SliceLostPostSearchResponses fetchSlicedLostAdoptions(Pageable pageable);
}

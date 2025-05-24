package kr.co.pawong.pwbe.lostPost.application.port.in;

import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAdoptionDetailDto;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.SliceLostPostSearchResponses;
import org.springframework.data.domain.Pageable;

public interface QueryLostAdoptionDataUseCase {

    LostAdoptionDetailDto findAdoptionById(Long adoptionId);

    SliceLostPostSearchResponses fetchSlicedLostAdoptions(Pageable pageable, Long userId);
}

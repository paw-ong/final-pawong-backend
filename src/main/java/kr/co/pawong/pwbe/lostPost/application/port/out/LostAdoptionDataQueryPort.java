package kr.co.pawong.pwbe.lostPost.application.port.out;

import kr.co.pawong.pwbe.lostPost.domain.LostAdoption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LostAdoptionDataQueryPort {

    LostAdoption findAdoptionByIdOrThrow(Long adoptionId);

    Page<LostAdoption> getLostAdoptionsPaged(Pageable pageable);
}

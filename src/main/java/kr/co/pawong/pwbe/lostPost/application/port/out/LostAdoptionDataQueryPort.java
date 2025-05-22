package kr.co.pawong.pwbe.lostPost.application.port.out;

import kr.co.pawong.pwbe.lostPost.domain.LostAdoption;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LostAdoptionDataQueryPort {

    LostAdoption findAdoptionById(Long adoptionId);

    Page<LostAdoption> getLostAdoptionsPaged(Pageable pageable);
}

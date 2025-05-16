package kr.co.pawong.pwbe.lostPost.application.port.out;

import kr.co.pawong.pwbe.lostPost.domain.LostAdoption;

public interface LostAdoptionDataQueryPort {

    LostAdoption findAdoptionById(Long adoptionId);

}

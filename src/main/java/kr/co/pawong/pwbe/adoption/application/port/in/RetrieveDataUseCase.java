package kr.co.pawong.pwbe.adoption.application.port.in;

import java.util.List;
import kr.co.pawong.pwbe.adoption.domain.model.Adoption;

import kr.co.pawong.pwbe.adoption.application.port.in.dto.SliceAdoptionSearchResponses;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionRecommendResponses;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionDetailResponse;
import org.springframework.data.domain.Pageable;


import kr.co.pawong.pwbe.shelter.application.port.in.dto.ShelterInfoDto;

public interface RetrieveDataUseCase {
    List<Adoption> getAllAdoptions();

    SliceAdoptionSearchResponses fetchSlicedAdoptions(Pageable pageable);

    ShelterInfoDto findShelterInfoByAdoptionId(Long adoptionId);

    AdoptionDetailResponse getAdoptionDetail(Long adoptionId);

    AdoptionRecommendResponses getRecommendAdoptions();
}

package kr.co.pawong.pwbe.lostPost.application.service;

import kr.co.pawong.pwbe.lostPost.application.port.in.QueryLostAdoptionDataUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAdoptionDetailDto;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAdoptionDetailResponse;
import kr.co.pawong.pwbe.lostPost.application.port.in.mapper.LostAdoptionDetailMapper;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostAdoptionDataQueryPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.ShelterCareNmPort;
import kr.co.pawong.pwbe.lostPost.domain.LostAdoption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueryLostAdoptionDataService implements QueryLostAdoptionDataUseCase {

    private final LostAdoptionDataQueryPort lostAdoptionDataQueryPort;
    private final ShelterCareNmPort shelterCareNmPort;

    @Override
    public LostAdoptionDetailResponse findAdoptionById(Long adoptionId) {

        LostAdoption lostAdoption = lostAdoptionDataQueryPort.findAdoptionById(adoptionId);
        String careNm = shelterCareNmPort.getShelterCareNmByCareRegNo(lostAdoption.getCareRegNo());

        LostAdoptionDetailDto lostAdoptionDetailDto
                = LostAdoptionDetailMapper.toModel(lostAdoption, careNm);
        return new LostAdoptionDetailResponse(lostAdoptionDetailDto);
    }

}

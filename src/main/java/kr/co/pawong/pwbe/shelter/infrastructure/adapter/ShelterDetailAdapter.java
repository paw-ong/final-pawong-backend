package kr.co.pawong.pwbe.shelter.infrastructure.adapter;

import kr.co.pawong.pwbe.adoption.application.port.out.ShelterDetailPort;
import kr.co.pawong.pwbe.shelter.presentation.controller.dto.ShelterDetailDto;
import kr.co.pawong.pwbe.shelter.presentation.port.ShelterQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShelterDetailAdapter implements ShelterDetailPort {

    private final ShelterQueryService shelterQueryService;

    public ShelterDetailDto getShelterDetail(String careRegNo) {
        return shelterQueryService.shelterDetail(careRegNo);
    }
}

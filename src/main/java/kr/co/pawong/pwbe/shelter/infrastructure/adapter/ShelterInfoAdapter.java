package kr.co.pawong.pwbe.shelter.infrastructure.adapter;

import kr.co.pawong.pwbe.adoption.application.port.out.ShelterInfoPort;
import kr.co.pawong.pwbe.shelter.presentation.controller.dto.ShelterInfoDto;
import kr.co.pawong.pwbe.shelter.presentation.port.ShelterQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShelterInfoAdapter implements ShelterInfoPort {

    private final ShelterQueryService shelterQueryService;

    @Override
    public ShelterInfoDto getShelterInfo(String careRegNo) {
        ShelterInfoDto shelterInfoDto = shelterQueryService.shelterInfo(careRegNo);
        if (shelterInfoDto == null) {
            log.warn("ShelterInfo not found for careRegNo: {}", careRegNo);
            return null; // 또는 예외 throw
        }
        return shelterInfoDto; // 또는 예외 throw
    }


}

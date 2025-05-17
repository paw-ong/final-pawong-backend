package kr.co.pawong.pwbe.adoption.adapter.out.shelter;

import kr.co.pawong.pwbe.adoption.application.port.out.dto.AdoptionCareDto;
import kr.co.pawong.pwbe.adoption.application.port.out.ShelterCommandPort;
import kr.co.pawong.pwbe.shelter.application.port.in.UpdateShelterDataUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShelterCommandAdapter implements ShelterCommandPort {

    private final UpdateShelterDataUseCase updateShelterDataUseCase;

    @Override
    public void processShelterInfo(AdoptionCareDto adoptionCareDto) {
        updateShelterDataUseCase.updateShelterIfNotExist(adoptionCareDto);
    }
}

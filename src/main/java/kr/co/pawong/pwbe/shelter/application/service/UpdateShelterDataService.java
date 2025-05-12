package kr.co.pawong.pwbe.shelter.application.service;

import kr.co.pawong.pwbe.shelter.domain.Shelter;
import kr.co.pawong.pwbe.shelter.application.port.in.dto.ShelterCreate;
import kr.co.pawong.pwbe.shelter.application.port.out.ShelterDataCommandPort;
import kr.co.pawong.pwbe.shelter.application.port.in.UpdateShelterDataUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateShelterDataService implements UpdateShelterDataUseCase {

    private final ShelterDataCommandPort shelterDataCommandPort;

    // ShelterCreate -> Shelter -> Repo에 전달
    @Transactional
    @Override
    public void saveShelters(List<ShelterCreate> shelterCreates) {
        List<Shelter> shelters = shelterCreates.stream()
                .map(Shelter::from)
                .toList();

        shelterDataCommandPort.saveShelters(shelters);
    }
}

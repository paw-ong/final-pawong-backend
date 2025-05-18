package kr.co.pawong.pwbe.shelter.application.port.in;

import java.util.List;
import kr.co.pawong.pwbe.adoption.application.port.out.dto.AdoptionCareDto;
import kr.co.pawong.pwbe.infrastructure.api.dto.ShelterCreate;


public interface UpdateShelterDataUseCase {
    // ShelterCreate -> Shelter
    void saveShelters(List<ShelterCreate> shelterCreates);

    void updateShelterIfNotExist(AdoptionCareDto adoptionCareDto);

}

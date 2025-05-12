package kr.co.pawong.pwbe.shelter.application.port.in;

import kr.co.pawong.pwbe.shelter.application.port.in.dto.ShelterCreate;
import java.util.List;


public interface UpdateShelterDataUseCase {
    // ShelterCreate -> Shelter
    void saveShelters(List<ShelterCreate> shelterCreates);

}

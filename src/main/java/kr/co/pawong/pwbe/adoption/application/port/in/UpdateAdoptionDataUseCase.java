package kr.co.pawong.pwbe.adoption.application.port.in;

import java.util.List;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionCreate;

public interface UpdateAdoptionDataUseCase {
    // AdoptionCreate -> Adoption
    void saveAdoptions(List<AdoptionCreate> adoptionCreates);

    void aiProcessAdoptions();

}

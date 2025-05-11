package kr.co.pawong.pwbe.adoption.application.port.in;

import java.util.List;
import kr.co.pawong.pwbe.adoption.application.port.out.dto.AdoptionCreate;

public interface UpdateDataUseCase {
    // AdoptionCreate -> Adoption
    void saveAdoptions(List<AdoptionCreate> adoptionCreates);

    void aiProcessAdoptions();

}

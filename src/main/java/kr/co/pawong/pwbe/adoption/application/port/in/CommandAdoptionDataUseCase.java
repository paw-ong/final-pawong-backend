package kr.co.pawong.pwbe.adoption.application.port.in;

import java.util.List;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionCreate;
import kr.co.pawong.pwbe.adoption.domain.model.Adoption;

public interface CommandAdoptionDataUseCase {

    void saveAdoptions(List<AdoptionCreate> adoptionCreates);

    Adoption processAdoption(Adoption adoption);
}

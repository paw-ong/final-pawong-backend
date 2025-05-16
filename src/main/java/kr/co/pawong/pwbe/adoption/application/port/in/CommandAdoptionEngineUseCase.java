package kr.co.pawong.pwbe.adoption.application.port.in;

import java.util.List;
import kr.co.pawong.pwbe.adoption.application.port.out.dto.AdoptionEsDto;
import kr.co.pawong.pwbe.adoption.domain.model.Adoption;

public interface CommandAdoptionEngineUseCase {

    AdoptionEsDto processAdoptionForEs(Adoption adoption);

    void saveEsAndUpdateIsEmbedded(List<AdoptionEsDto> adoptionEsDtos);
}

package kr.co.pawong.pwbe.adoption.application.port.out;

import java.util.List;
import kr.co.pawong.pwbe.adoption.application.port.out.dto.AdoptionEsDto;

public interface AdoptionEngineCommandPort {
    // AdoptionEsDto -> AdoptionDocument -> ES
    void saveAdoptionToEs(List<AdoptionEsDto> adoptionEsDtos);
}

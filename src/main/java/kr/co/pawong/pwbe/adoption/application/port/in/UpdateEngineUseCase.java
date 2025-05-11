package kr.co.pawong.pwbe.adoption.application.port.in;

import java.util.List;
import kr.co.pawong.pwbe.adoption.domain.model.Adoption;

public interface UpdateEngineUseCase {

    void saveAdoptionToEs(List<Adoption> adoptions);
}

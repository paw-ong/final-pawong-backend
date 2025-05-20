package kr.co.pawong.pwbe.user.application.port.out;

import kr.co.pawong.pwbe.adoption.domain.model.Adoption;

public interface AdoptionInfoPort {
    Adoption findByIdOrThrow(Long adoptionId);
}

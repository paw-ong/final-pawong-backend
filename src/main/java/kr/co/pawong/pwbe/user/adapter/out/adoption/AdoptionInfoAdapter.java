package kr.co.pawong.pwbe.user.adapter.out.adoption;

import kr.co.pawong.pwbe.adoption.application.port.in.QueryAdoptionDataUseCase;
import kr.co.pawong.pwbe.adoption.domain.model.Adoption;
import kr.co.pawong.pwbe.user.application.port.out.AdoptionInfoPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdoptionInfoAdapter implements AdoptionInfoPort {

    private final QueryAdoptionDataUseCase queryAdoptionDataUsecase;

    @Override
    public Adoption findByIdOrThrow(Long adoptionId) {
        return queryAdoptionDataUsecase.findAdoptionByIdOrThrow(adoptionId);
    }
}

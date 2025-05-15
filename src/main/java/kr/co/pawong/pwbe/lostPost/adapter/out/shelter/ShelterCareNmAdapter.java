package kr.co.pawong.pwbe.lostPost.adapter.out.shelter;

import kr.co.pawong.pwbe.lostPost.application.port.out.ShelterCareNmPort;
import kr.co.pawong.pwbe.shelter.application.port.in.QueryCareNmUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShelterCareNmAdapter implements ShelterCareNmPort {

    private final QueryCareNmUseCase queryCareNmUseCase;

    @Override
    public  String getShelterCareNmByCareRegNo(String careRegNo){
        return queryCareNmUseCase.getShelterCareNmByCareRegNo(careRegNo);
    }

}

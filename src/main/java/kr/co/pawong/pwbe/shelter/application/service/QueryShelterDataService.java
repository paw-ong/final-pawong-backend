package kr.co.pawong.pwbe.shelter.application.service;

import java.util.Optional;
import kr.co.pawong.pwbe.shelter.application.port.in.QueryCareNmUseCase;
import kr.co.pawong.pwbe.shelter.domain.Shelter;
import kr.co.pawong.pwbe.shelter.application.port.out.ShelterDataQueryPort;
import kr.co.pawong.pwbe.shelter.application.port.in.dto.ShelterDetailDto;
import kr.co.pawong.pwbe.shelter.application.port.in.dto.ShelterInfoDto;
import kr.co.pawong.pwbe.shelter.application.port.in.QueryShelterDataUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class QueryShelterDataService implements QueryShelterDataUseCase, QueryCareNmUseCase {

    private final ShelterDataQueryPort shelterDataQueryPort;

    @Override
    public ShelterInfoDto shelterInfo(String careRegNo) {
        Shelter shelter = shelterDataQueryPort.findByCareRegNoOrThrow(careRegNo);

        ShelterInfoDto shelterInfoDto = ShelterInfoDto.from(shelter);

        if (shelterInfoDto == null) {
            return new ShelterInfoDto(careRegNo, "", "");
        }

        return shelterInfoDto;
    }

    @Override
    public ShelterDetailDto shelterDetail(String careRegNo){
        Shelter shelter = shelterDataQueryPort.findByCareRegNoOrThrow(careRegNo);

        return ShelterDetailDto.from(shelter);
    }

    @Override
    @Transactional(readOnly = true)
    public String getShelterCareNmByCareRegNo(String careRegNo) {
        return Optional.ofNullable(shelterDataQueryPort.findByCareRegNoOrThrow(careRegNo))
                .map(Shelter::getCareNm)
                .orElse("");
    }
}

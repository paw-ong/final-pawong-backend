package kr.co.pawong.pwbe.shelter.application.port.in;

import kr.co.pawong.pwbe.shelter.application.port.in.dto.ShelterDetailDto;
import kr.co.pawong.pwbe.shelter.application.port.in.dto.ShelterInfoDto;

public interface QueryShelterDataUseCase {

    ShelterInfoDto shelterInfo(String careRegNo);

    ShelterDetailDto shelterDetail(String careRegNo);
}

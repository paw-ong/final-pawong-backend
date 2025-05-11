package kr.co.pawong.pwbe.adoption.application.port.out;

import kr.co.pawong.pwbe.shelter.application.port.in.dto.ShelterDetailDto;
import kr.co.pawong.pwbe.shelter.application.port.in.dto.ShelterInfoDto;

public interface ShelterInfoPort {

    ShelterInfoDto getShelterInfo(String careRegNo);

    ShelterDetailDto getShelterDetail(String careRegNo);
}

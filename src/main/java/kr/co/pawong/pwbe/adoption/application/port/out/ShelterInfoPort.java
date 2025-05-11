package kr.co.pawong.pwbe.adoption.application.port.out;

import kr.co.pawong.pwbe.shelter.presentation.controller.dto.ShelterInfoDto;

public interface ShelterInfoPort {
    ShelterInfoDto getShelterInfo(String careRegNo);
}

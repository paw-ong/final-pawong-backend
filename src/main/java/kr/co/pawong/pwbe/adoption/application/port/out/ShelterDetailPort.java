package kr.co.pawong.pwbe.adoption.application.port.out;

import kr.co.pawong.pwbe.shelter.presentation.controller.dto.ShelterDetailDto;

public interface ShelterDetailPort {
    ShelterDetailDto getShelterDetail(String careRegNo);
}

package kr.co.pawong.pwbe.adoption.application.port.out;

import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionCareDto;

public interface ShelterCommandPort {

    void processShelterInfo(AdoptionCareDto adoptionCareDto);

}

package kr.co.pawong.pwbe.lostPost.application.port.out;

import kr.co.pawong.pwbe.lostPost.application.port.out.dto.LostAnimalDto;

public interface LostAnimalEngineCommandPort {
    void saveLostAnimalToEs(LostAnimalDto lostAnimalDto);

}

package kr.co.pawong.pwbe.shelter.application.port.out;

import kr.co.pawong.pwbe.shelter.domain.Shelter;
import java.util.List;


public interface ShelterDataCommandPort {
    // 동물보호센터 RDB저장
    void saveShelters(List<Shelter> shelter);

    List<String> findAllCareRegNos();

    void saveShelter(Shelter shelter);

}

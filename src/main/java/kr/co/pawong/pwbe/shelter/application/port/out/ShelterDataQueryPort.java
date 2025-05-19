package kr.co.pawong.pwbe.shelter.application.port.out;

import kr.co.pawong.pwbe.shelter.domain.Shelter;


public interface ShelterDataQueryPort {

    Shelter findByCareRegNoOrThrow(String careRegNo);

    boolean existsByCareRegNo(String careRegNo);


}

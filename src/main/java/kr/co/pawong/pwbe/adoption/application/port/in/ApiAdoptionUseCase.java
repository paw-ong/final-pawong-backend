package kr.co.pawong.pwbe.adoption.application.port.in;

import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionCreate;
import kr.co.pawong.pwbe.adoption.application.service.dto.AdoptionApi;

public interface ApiAdoptionUseCase {

    AdoptionCreate convertToAdoptionCreate(AdoptionApi.Item item);

    AdoptionApi.Item fetchNextAdoptionItem();

    void extractAndProcessShelterInfo(AdoptionApi.Item item);
}

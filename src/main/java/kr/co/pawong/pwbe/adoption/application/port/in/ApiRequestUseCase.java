package kr.co.pawong.pwbe.adoption.application.port.in;

import java.util.List;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionCreate;
import kr.co.pawong.pwbe.adoption.application.service.dto.AdoptionApi;

public interface ApiRequestUseCase {

    List<AdoptionCreate> convertToAdoptionCreates(List<AdoptionApi.Item> items);

    AdoptionApi.Item fetchNextAdoptionItem();
}

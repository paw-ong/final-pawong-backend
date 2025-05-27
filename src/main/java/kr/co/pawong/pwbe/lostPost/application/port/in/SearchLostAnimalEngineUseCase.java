package kr.co.pawong.pwbe.lostPost.application.port.in;

import java.util.List;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAnimalEngineRequest;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAnimalEngineResponse;

public interface SearchLostAnimalEngineUseCase {

    List<LostAnimalEngineResponse> searchSimilarLostAnimals(LostAnimalEngineRequest lostAnimalEngineRequest);
}

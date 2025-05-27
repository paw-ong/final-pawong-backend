package kr.co.pawong.pwbe.lostPost.application.port.out;

import java.util.List;
import kr.co.pawong.pwbe.lostPost.application.port.out.dto.LostAnimalEngineRequest;
import kr.co.pawong.pwbe.lostPost.application.port.out.dto.LostAnimalEngineResponse;

public interface LostAnimalEngineQueryPort {

    List<LostAnimalEngineResponse> searchSimilarLostAnimals(LostAnimalEngineRequest request);
}

package kr.co.pawong.pwbe.lostPost.application.port.out;

import java.util.List;
import java.util.Optional;
import kr.co.pawong.pwbe.lostPost.application.port.out.dto.LostAnimalEngineRequest;
import kr.co.pawong.pwbe.lostPost.application.port.out.dto.LostAnimalEngineResponse;
import kr.co.pawong.pwbe.lostPost.enums.PostType;

public interface LostAnimalEngineQueryPort {

    List<LostAnimalEngineResponse> searchSimilarLostAnimals(LostAnimalEngineRequest request);

    List<LostAnimalEngineResponse> searchSimilarLostAnimalsByESId(String esId, List<PostType> types);
}

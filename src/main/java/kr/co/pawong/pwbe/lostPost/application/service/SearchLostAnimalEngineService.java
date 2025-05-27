package kr.co.pawong.pwbe.lostPost.application.service;

import java.util.List;
import kr.co.pawong.pwbe.lostPost.application.port.in.SearchLostAnimalEngineUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAnimalEngineRequest;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAnimalEngineResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchLostAnimalEngineService implements SearchLostAnimalEngineUseCase {

    public List<LostAnimalEngineResponse> searchSimilarLostAnimals(
            LostAnimalEngineRequest lostAnimalEngineRequest) {
        return List.of();
    }
}

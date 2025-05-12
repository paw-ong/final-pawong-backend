package kr.co.pawong.pwbe.adoption.adapter.in.api;

import kr.co.pawong.pwbe.adoption.adapter.in.api.dto.request.AdoptionSearchRequest;
import kr.co.pawong.pwbe.adoption.adapter.in.api.dto.response.AdoptionAutocompleteResponse;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionSearchResponses;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionIdSearchResponses;
import kr.co.pawong.pwbe.adoption.application.port.in.SearchAdoptionEngineUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/adoptions")
@RestController
@RequiredArgsConstructor
public class AdoptionSearchController {

    private final SearchAdoptionEngineUseCase searchAdoptionEngineUseCase;

    @GetMapping("/search")
    public ResponseEntity<AdoptionSearchResponses> search(@ModelAttribute AdoptionSearchRequest request) {
        AdoptionSearchResponses response = searchAdoptionEngineUseCase.search(request);
        return ResponseEntity.ok(response);
    }

    // adoptionId 리스트만 반환하는 테스트용 API
    @GetMapping("/test/search")
    public ResponseEntity<AdoptionIdSearchResponses> searchDocumentIds(@ModelAttribute AdoptionSearchRequest request) {
        AdoptionIdSearchResponses response = searchAdoptionEngineUseCase.searchDocumentIds(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/autocomplete")
    public ResponseEntity<AdoptionAutocompleteResponse> searchAutoComplete(
            @RequestParam String keyword
    ) {
        List<String> autocomplete = searchAdoptionEngineUseCase.autocomplete(keyword);
        return ResponseEntity.ok(
                new AdoptionAutocompleteResponse(autocomplete)
        );
    }
}
package kr.co.pawong.pwbe.adoption.adapter.in.controller;

import kr.co.pawong.pwbe.adoption.adapter.in.controller.dto.request.AdoptionSearchRequest;
import kr.co.pawong.pwbe.adoption.adapter.in.controller.dto.response.AdoptionAutocompleteResponse;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionSearchResponses;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionIdSearchResponses;
import kr.co.pawong.pwbe.adoption.application.port.in.SearchEngineUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/adoptions")
@RestController
@RequiredArgsConstructor
public class AdoptionSearchController {

    private final SearchEngineUseCase searchEngineUseCase;

    @GetMapping("/search")
    public ResponseEntity<AdoptionSearchResponses> search(@ModelAttribute AdoptionSearchRequest request) {
        AdoptionSearchResponses response = searchEngineUseCase.search(request);
        return ResponseEntity.ok(response);
    }

    // adoptionId 리스트만 반환하는 테스트용 API
    @GetMapping("/test/search")
    public ResponseEntity<AdoptionIdSearchResponses> searchDocumentIds(@ModelAttribute AdoptionSearchRequest request) {
        AdoptionIdSearchResponses response = searchEngineUseCase.searchDocumentIds(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/autocomplete")
    public ResponseEntity<AdoptionAutocompleteResponse> searchAutoComplete(
            @RequestParam String keyword
    ) {
        List<String> autocomplete = searchEngineUseCase.autocomplete(keyword);
        return ResponseEntity.ok(
                new AdoptionAutocompleteResponse(autocomplete)
        );
    }
}
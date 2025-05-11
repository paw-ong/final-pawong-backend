package kr.co.pawong.pwbe.adoption.adapter.in.controller;

import kr.co.pawong.pwbe.adoption.application.port.in.dto.SliceAdoptionSearchResponses;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionRecommendResponses;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionDetailResponse;
import kr.co.pawong.pwbe.adoption.application.port.in.RetrieveDataUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/adoptions")
@RequiredArgsConstructor
public class AdoptionQueryController {

    private final RetrieveDataUseCase retrieveDataUseCase;

    // slice 방식 (무한 스크롤)
    @GetMapping("")
    public ResponseEntity<SliceAdoptionSearchResponses> getSlicedAdoptions(
            @PageableDefault(page = 0, size = 20, sort = "noticeSdt", direction = Sort.Direction.DESC) Pageable pageable) {

        SliceAdoptionSearchResponses response = retrieveDataUseCase.fetchSlicedAdoptions(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recommend")
    public ResponseEntity<AdoptionRecommendResponses> getRecommendAdoptions() {
        AdoptionRecommendResponses response = retrieveDataUseCase.getRecommendAdoptions();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdoptionDetailResponse> getAdoptionDetail(
            @PathVariable("id") Long adoptionId) {
        var response = retrieveDataUseCase.getAdoptionDetail(adoptionId);
        return ResponseEntity.ok(response);
    }
}
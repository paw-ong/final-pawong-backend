package kr.co.pawong.pwbe.lostPost.adapter.in.api;

import kr.co.pawong.pwbe.lostPost.application.port.in.QueryLostAdoptionDataUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAdoptionDetailResponse;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostDetailResponse;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.SliceLostPostSearchResponses;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lost-animals")
@RequiredArgsConstructor
public class LostAdoptionQueryController {

    private final QueryLostAdoptionDataUseCase queryLostAdoptionDataUseCase;

    @GetMapping("/lost-adoptions/{id}")
    public ResponseEntity<LostAdoptionDetailResponse> getLostDetail(
            @PathVariable("id") Long Id) {
        LostAdoptionDetailResponse response = queryLostAdoptionDataUseCase.findAdoptionById(Id);
        return ResponseEntity.ok(response);
    }

    // slice 방식 (무한 스크롤)
    @GetMapping("/adoption")
    public ResponseEntity<SliceLostPostSearchResponses> getSlicedLostAdoptions(
            @PageableDefault(page = 0, size = 20, sort = "lostAdoptionId", direction = Sort.Direction.DESC) Pageable pageable) {

        SliceLostPostSearchResponses responses = queryLostAdoptionDataUseCase.fetchSlicedLostAdoptions(pageable);
        return ResponseEntity.ok(responses);

    }

}

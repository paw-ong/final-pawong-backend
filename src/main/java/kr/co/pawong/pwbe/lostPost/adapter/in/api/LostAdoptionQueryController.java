package kr.co.pawong.pwbe.lostPost.adapter.in.api;

import kr.co.pawong.pwbe.lostPost.application.port.in.QueryLostAdoptionDataUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAdoptionDetailDto;
import kr.co.pawong.pwbe.lostPost.adapter.in.api.dto.response.LostAdoptionDetailResponse;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.SliceLostPostSearchResponses;
import kr.co.pawong.pwbe.user.adapter.out.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lost-animals")
@RequiredArgsConstructor
public class LostAdoptionQueryController {

    private final QueryLostAdoptionDataUseCase queryLostAdoptionDataUseCase;

    @GetMapping("/lost-adoptions/{id}")
    public ResponseEntity<LostAdoptionDetailResponse> getLostDetail(
            @PathVariable("id") Long Id) {
        LostAdoptionDetailDto lostAdoptionDetailDto = queryLostAdoptionDataUseCase.findAdoptionById(Id);

        return ResponseEntity.ok(new LostAdoptionDetailResponse(lostAdoptionDetailDto));
    }

    // slice 방식 (무한 스크롤)
    @GetMapping("/adoption")
    public ResponseEntity<SliceLostPostSearchResponses> getSlicedLostAdoptions(
            @PageableDefault(page = 0, size = 20, sort = "happenDt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal(errorOnInvalidType = false)
            CustomUserDetails principal) {
        Long userId = (principal != null ? principal.getUserId() : null);
        SliceLostPostSearchResponses responses = queryLostAdoptionDataUseCase.fetchSlicedLostAdoptions(pageable, userId);
        return ResponseEntity.ok(responses);

    }

}

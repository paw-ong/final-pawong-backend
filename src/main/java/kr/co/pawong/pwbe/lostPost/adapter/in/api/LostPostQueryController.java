package kr.co.pawong.pwbe.lostPost.adapter.in.api;

import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostDetailDto;
import kr.co.pawong.pwbe.lostPost.adapter.in.api.dto.response.LostPostDetailResponse;
import kr.co.pawong.pwbe.lostPost.application.port.in.QueryLostPostDataUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.SliceLostPostSearchResponses;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import kr.co.pawong.pwbe.global.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lost-animals")
@RequiredArgsConstructor
public class LostPostQueryController {

    private final QueryLostPostDataUseCase queryLostPostDataUseCase;


    @GetMapping("/lost-posts/{id}")
    public ResponseEntity<LostPostDetailResponse> getLostDetail(
            @PathVariable("id") Long Id) {
        LostPostDetailDto lostPostDetailDto = queryLostPostDataUseCase.findLostPostById(Id);
        return ResponseEntity.ok(new LostPostDetailResponse(lostPostDetailDto));
    }

    // slice 방식 (무한 스크롤)
    @GetMapping("")
    public ResponseEntity<SliceLostPostSearchResponses> getSlicedLostPosts(
            @PageableDefault(page = 0, size = 20, sort = "lostPostId", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "type", required = false) PostType type,
            @AuthenticationPrincipal(errorOnInvalidType = false)
            CustomUserDetails principal
            ) {
        Long userId = (principal != null ? principal.getUserId() : null);
        SliceLostPostSearchResponses responses = queryLostPostDataUseCase.fetchSlicedLostPosts(pageable,type,userId);
        return ResponseEntity.ok(responses);

    }


}

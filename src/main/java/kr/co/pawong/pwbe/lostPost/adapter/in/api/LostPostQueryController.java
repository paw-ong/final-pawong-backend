package kr.co.pawong.pwbe.lostPost.adapter.in.api;

import kr.co.pawong.pwbe.lostPost.application.port.in.QueryLostAdoptionDataUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAdoptionDetailResponse;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostDetailResponse;
import kr.co.pawong.pwbe.lostPost.application.port.in.QueryLostPostDataUseCase;
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
public class LostPostQueryController {

    private final QueryLostPostDataUseCase queryLostPostDataUseCase;
    private final QueryLostAdoptionDataUseCase queryLostAdoptionDataUseCase;

    @GetMapping("/{id}")
    public ResponseEntity<?> getLostDetail(
            @PathVariable("id") Long Id,
            @RequestParam(value = "type", required = false) String type) {
        if (type != null && type.equals("lost-posts")) {
            LostPostDetailResponse response = queryLostPostDataUseCase.findLostPostById(Id);
            return ResponseEntity.ok(response);
        } else if (type != null && type.equals("lost-adoptions")) {
            LostAdoptionDetailResponse response = queryLostAdoptionDataUseCase.findAdoptionById(Id);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body("유효하지 않은 type 파라미터입니다.");
    }

    // slice 방식 (무한 스크롤)
    @GetMapping("")
    public ResponseEntity<SliceLostPostSearchResponses> getSlicedLostPosts(
            @PageableDefault(page = 0, size = 20, sort = "lostPostId", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "type", required = false) PostType type ) {

        SliceLostPostSearchResponses responses = queryLostPostDataUseCase.fetchSlicedLostPosts(pageable,type);
        return ResponseEntity.ok(responses);

    }


}

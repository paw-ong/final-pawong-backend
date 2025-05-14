package kr.co.pawong.pwbe.lostPost.adapter.in.api;

import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostDetailResponse;
import kr.co.pawong.pwbe.lostPost.application.port.in.QueryLostPostDataUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lost-posts")
@RequiredArgsConstructor
public class LostPostQueryController {

    private final QueryLostPostDataUseCase queryLostPostDataUseCase;

    @GetMapping("/{id}")
    public ResponseEntity<LostPostDetailResponse>getLostPostDetail(
            @PathVariable("id") Long LostPostId ) {
        LostPostDetailResponse response = queryLostPostDataUseCase.findLostPostById(LostPostId);
        return ResponseEntity.ok(response);
    }
}

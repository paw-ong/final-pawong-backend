package kr.co.pawong.pwbe.lostPost.adapter.in.api;

import kr.co.pawong.pwbe.lostPost.adapter.in.api.dto.request.LostPostSearchRequest;
import kr.co.pawong.pwbe.lostPost.application.port.in.SearchLostPostDataUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostSearchResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/lost-animals")
@RestController
@RequiredArgsConstructor
public class LostPostSearchController {

    private final SearchLostPostDataUseCase searchLostPostDataUseCase;

    @GetMapping("/search")
    public ResponseEntity<LostPostSearchResponses> search(@ModelAttribute LostPostSearchRequest request,
            @PageableDefault(page = 0, size = 20, sort = "lostPostId", direction = Sort.Direction.DESC) Pageable pageable) {
        LostPostSearchResponses responses = searchLostPostDataUseCase.searchLostPosts(request, pageable);
        return ResponseEntity.ok(responses);
    }
}

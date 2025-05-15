package kr.co.pawong.pwbe.lostPost.adapter.in.api;

import java.util.List;
import kr.co.pawong.pwbe.lostPost.application.port.in.QueryLostPostDataUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.SliceLostPostSearchResponses;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lost-animals")
@RequiredArgsConstructor
public class LostPostQueryController {

    private final QueryLostPostDataUseCase lostPostQueryUseCase;

    @GetMapping("")
    public ResponseEntity<List<LostPostCard>> getLostPosts(
            @RequestParam(value = "type", required = false) PostType type
    ) {

            List<LostPostCard> lostPostCards= lostPostQueryUseCase.getLostPostsByPostType(type);
            return ResponseEntity.ok(lostPostCards);

    }


}

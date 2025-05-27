package kr.co.pawong.pwbe.lostPost.adapter.in.api;

import java.util.List;
import kr.co.pawong.pwbe.lostPost.application.port.in.SearchSimilarLostAnimalsUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.SimilarLostAnimalsResponse;
import kr.co.pawong.pwbe.user.adapter.out.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/lost-animals")
@RequiredArgsConstructor
public class LostAnimalQueryController {

    private final SearchSimilarLostAnimalsUseCase searchSimilarLostAnimalsUseCase;

    @GetMapping("/lost-posts/{lostPostId}/similar-animals")
    public ResponseEntity<SimilarLostAnimalsResponse> searchSimilarLostAnimals(
            @PathVariable long lostPostId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails == null ? null : userDetails.getUserId();
        // 유사 LostAnimal 조회
        List<LostPostCard> result = searchSimilarLostAnimalsUseCase.searchSimilarLostAnimals(
                userId, lostPostId);
        // null_일 경우 아직 인덱싱 되지 않은 것.
        if (result == null) {
            return ResponseEntity.accepted().build();
        }
        // 인덱싱이 이미 완료되어서 유사한 것을 검색한 결과를 반환
        return ResponseEntity.ok(new SimilarLostAnimalsResponse(result));
    }
}

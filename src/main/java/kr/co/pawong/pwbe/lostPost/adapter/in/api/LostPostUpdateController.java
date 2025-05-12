package kr.co.pawong.pwbe.lostPost.adapter.in.api;

import kr.co.pawong.pwbe.lostPost.domain.LostPostCreate;
import kr.co.pawong.pwbe.lostPost.adapter.in.api.dto.response.LostPostCreateResponse;
import kr.co.pawong.pwbe.lostPost.application.port.in.LostPostUpdateUseCase;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import kr.co.pawong.pwbe.user.infrastructure.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lost-post")
@RequiredArgsConstructor
public class LostPostUpdateController {

    private final LostPostUpdateUseCase lostPostUpdateUseCase;

    @PostMapping
    public ResponseEntity<LostPostCreateResponse> createLostPost(
            @RequestBody LostPostCreate lostPostCreate,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        LostPost lostPost = lostPostUpdateUseCase.createLostPost(lostPostCreate.toDomain(), userDetails.getUserId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new LostPostCreateResponse(lostPost.getLostPostId()));
    }
}

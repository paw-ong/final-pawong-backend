package kr.co.pawong.pwbe.lostPost.adapter.in.api;

import kr.co.pawong.pwbe.lostPost.adapter.in.api.dto.request.LostPostCreateRequest;
import kr.co.pawong.pwbe.lostPost.adapter.in.api.dto.response.LostPostCreateResponse;
import kr.co.pawong.pwbe.lostPost.application.port.in.CommandLostPostDataUseCase;
import kr.co.pawong.pwbe.user.adapter.out.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lost-posts")
@RequiredArgsConstructor
public class LostPostCommandController {

    private final CommandLostPostDataUseCase lostPostUpdateUseCase;

    @PostMapping
    public ResponseEntity<LostPostCreateResponse> createLostPost(
            @RequestBody LostPostCreateRequest lostPostCreate,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long createdId = lostPostUpdateUseCase.createLostPost(
                lostPostCreate.toDomain(),
                userDetails.getUserId()
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new LostPostCreateResponse(createdId));
    }
}

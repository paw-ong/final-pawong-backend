package kr.co.pawong.pwbe.lostPost.adapter.in.api;

import kr.co.pawong.pwbe.lostPost.adapter.in.api.dto.request.LostPostCreateRequest;
import kr.co.pawong.pwbe.lostPost.adapter.in.api.dto.request.LostPostUpdateRequest;
import kr.co.pawong.pwbe.lostPost.adapter.in.api.dto.response.LostPostCreateResponse;
import kr.co.pawong.pwbe.lostPost.application.port.in.CommandLostPostDataUseCase;
import kr.co.pawong.pwbe.user.adapter.in.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lost-animals")
@RequiredArgsConstructor
public class LostPostCommandController {

    private final CommandLostPostDataUseCase lostPostUpdateUseCase;

    @PostMapping("/lost-posts")
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

    @PutMapping("/lost-posts/{postId}")
    public ResponseEntity<Long> updateLostPost(
            @PathVariable("postId") Long postId,
            @RequestBody LostPostUpdateRequest lostPostUpdate,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(
                lostPostUpdateUseCase.updateLostPost(
                        postId,
                        lostPostUpdate.toDomain(),
                        userDetails.getUserId()));
    }

    @DeleteMapping("/lost-posts/{postId}")
    public ResponseEntity<Long> deleteLostPost(
            @PathVariable("postId") Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        lostPostUpdateUseCase.deleteLostPost(postId, userDetails.getUserId());
        return ResponseEntity.ok(postId);
    }
}

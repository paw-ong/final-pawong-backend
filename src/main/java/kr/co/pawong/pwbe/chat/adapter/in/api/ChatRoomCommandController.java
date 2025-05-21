package kr.co.pawong.pwbe.chat.adapter.in.api;

import kr.co.pawong.pwbe.chat.adapter.in.api.dto.request.ChatRoomCreateRequest;
import kr.co.pawong.pwbe.chat.adapter.in.api.dto.response.ChatRoomCreateResponse;
import kr.co.pawong.pwbe.chat.application.port.in.CommandChatRoomDataUseCase;
import kr.co.pawong.pwbe.user.adapter.out.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 채팅방 생성을 위한 컨트롤러
 */
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRoomCommandController {

    // TODO 구현
    private final CommandChatRoomDataUseCase commandChatDataUseCase;

    /**
     * @param {lostPostId, authorId} as ChatRoomCreateRequest
     * @param @AuthenticationPrincipal CustomUserDetails
     * @return roomId
     */
    @PostMapping("/rooms")
    public ResponseEntity<ChatRoomCreateResponse> createChatRoom(
            @RequestBody ChatRoomCreateRequest chatRoomCreateRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long createdId = commandChatDataUseCase.createChatRoom(
                userDetails.getUserId(),
                chatRoomCreateRequest
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ChatRoomCreateResponse(createdId));
    }
}

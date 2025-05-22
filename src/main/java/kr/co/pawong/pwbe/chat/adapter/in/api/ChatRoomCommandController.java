package kr.co.pawong.pwbe.chat.adapter.in.api;

import kr.co.pawong.pwbe.chat.adapter.in.api.dto.request.ChatRoomCreateRequest;
import kr.co.pawong.pwbe.chat.adapter.in.api.dto.request.ChatRoomDeactivateRequest;
import kr.co.pawong.pwbe.chat.adapter.in.api.dto.response.ChatRoomCreateResponse;
import kr.co.pawong.pwbe.chat.adapter.in.api.dto.response.ChatRoomDeactivateResponse;
import kr.co.pawong.pwbe.chat.application.port.in.CommandChatRoomDataUseCase;
import kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
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

    private final CommandChatRoomDataUseCase commandChatRoomDataUseCase;

    /**
     * @param {lostPostId,             authorId} as ChatRoomCreateRequest
     * @param @AuthenticationPrincipal CustomUserDetails
     * @return roomId
     */
    @PostMapping("/rooms")
    public ResponseEntity<ChatRoomCreateResponse> createChatRoom(
            @RequestBody ChatRoomCreateRequest chatRoomCreateRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        // 사용자가 자신과의 채팅방을 생성하려는 경우 예외 발생
        if (userDetails.getUserId().equals(chatRoomCreateRequest.getAuthorId())) {
            throw new BaseException(CustomErrorCode.CHATROOM_POST_ERROR);
        }

        Long createdId = commandChatRoomDataUseCase.createChatRoom(
                userDetails.getUserId(),
                chatRoomCreateRequest
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ChatRoomCreateResponse(createdId));
    }

    @PostMapping("/rooms/deactivate")
    public ResponseEntity<ChatRoomDeactivateResponse> deactivateChatRoom(
            @RequestBody ChatRoomDeactivateRequest chatRoomDeactivateRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();
        Long chatRoomId = chatRoomDeactivateRequest.getChatRoomId();
        boolean deactivated = commandChatRoomDataUseCase.deactivateChatRoom(userId, chatRoomId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ChatRoomDeactivateResponse(deactivated));
    }
}

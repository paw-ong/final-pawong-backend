package kr.co.pawong.pwbe.chat.adapter.in.api;

import java.util.List;
import kr.co.pawong.pwbe.chat.adapter.in.api.dto.response.ChatRoomContainsUserResponse;
import kr.co.pawong.pwbe.chat.adapter.in.api.dto.response.ChatRoomStatusResponse;
import kr.co.pawong.pwbe.chat.adapter.in.api.dto.response.ChatRoomsResponse;
import kr.co.pawong.pwbe.chat.application.port.in.QueryChatRoomDataUseCase;
import kr.co.pawong.pwbe.chat.application.port.in.dto.ChatRoomDetail;
import kr.co.pawong.pwbe.global.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 채팅방 목록을 조회하기 위한 컨트롤러
 */
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRoomQueryController {

    private final QueryChatRoomDataUseCase queryChatDataUseCase;

    /**
     * 유저의 채팅방 목록을 조회하는 api
     *
     * @param userDetails
     * @return List<ChatRoomDetail>
     */
    @GetMapping(value = "/rooms", params = "!postId")
    public ResponseEntity<ChatRoomsResponse> findChatRooms(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();
        List<ChatRoomDetail> allChatRooms = queryChatDataUseCase.findUserChatRooms(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ChatRoomsResponse(allChatRooms));
    }

    /**
     * 실종 공고로 요청된 채팅방들 목록을 조회하는 api 내부적으로 api를 호출한 사용자가 해당 공고의 작성자가 아니면 예외를 던지도록 구현
     *
     * @param postId
     * @param userDetails
     * @return List<ChatRoomDetail>
     */
    @GetMapping(value = "/rooms", params = "postId")
    public ResponseEntity<ChatRoomsResponse> findChatRoomsByPostId(
            @RequestParam("postId") Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();
        List<ChatRoomDetail> chatRoomsByPost = queryChatDataUseCase.findUserChatRoomsByPostId(
                userId, postId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ChatRoomsResponse(chatRoomsByPost));
    }

    /**
     * 유저가 채팅방에 존재하는지 확인하는 api
     *
     * @param roomId
     * @param userDetails
     * @return boolean (존재 여부)
     */
    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<ChatRoomContainsUserResponse> checkUserInChatRoom(
            @PathVariable Long roomId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ChatRoomContainsUserResponse(
                        queryChatDataUseCase.isUserInChatRoom(userId, roomId)));
    }

    /**
     * 유저가 채팅방에 존재하는지 확인하는 api
     *
     * @param roomId
     * @return boolean (존재 여부)
     */
    @GetMapping("/rooms/{roomId}/status")
    public ResponseEntity<ChatRoomStatusResponse> checkRoomStatus(
            @PathVariable Long roomId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ChatRoomStatusResponse(
                        queryChatDataUseCase.isChatRoomActive(roomId)
                ));
    }
}
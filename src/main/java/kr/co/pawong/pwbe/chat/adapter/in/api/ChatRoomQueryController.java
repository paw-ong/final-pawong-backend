package kr.co.pawong.pwbe.chat.adapter.in.api;

import java.util.List;
import kr.co.pawong.pwbe.chat.adapter.in.api.dto.response.ChatRoomContainsUserResponse;
import kr.co.pawong.pwbe.chat.adapter.in.api.dto.response.ChatRoomsResponse;
import kr.co.pawong.pwbe.chat.application.port.in.QueryChatRoomDataUseCase;
import kr.co.pawong.pwbe.chat.application.port.in.dto.ChatRoomDetail;
import kr.co.pawong.pwbe.user.adapter.out.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
     * @param userDetails
     * @return List<ChatRoomDetail>
     */
    @GetMapping("/rooms")
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
     * 유저가 채팅방에 존재하는지 확인하는 api
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
                        queryChatDataUseCase.userExistsInChatRoom(userId, roomId)));
    }
}
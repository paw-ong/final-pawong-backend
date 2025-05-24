package kr.co.pawong.pwbe.chat.adapter.in.api;

import java.util.List;
import kr.co.pawong.pwbe.chat.adapter.in.api.dto.response.ChatRoomsResponse;
import kr.co.pawong.pwbe.chat.application.port.in.QueryChatRoomDataUseCase;
import kr.co.pawong.pwbe.chat.application.port.in.dto.ChatRoomDetail;
import kr.co.pawong.pwbe.user.adapter.out.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
}

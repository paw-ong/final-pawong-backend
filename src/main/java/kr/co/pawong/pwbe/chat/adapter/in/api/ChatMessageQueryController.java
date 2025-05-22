package kr.co.pawong.pwbe.chat.adapter.in.api;

import java.util.List;
import kr.co.pawong.pwbe.chat.adapter.in.api.dto.response.ChatMessagesResponse;
import kr.co.pawong.pwbe.chat.application.port.in.QueryChatMessageDataUseCase;
import kr.co.pawong.pwbe.chat.application.port.in.dto.ChatMessageDetail;
import kr.co.pawong.pwbe.user.adapter.out.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatMessageQueryController {

    private final QueryChatMessageDataUseCase queryChatMessageDataUseCase;

    @GetMapping("/{roomId}")
    public ResponseEntity<ChatMessagesResponse> findAllMessagesInChatRoom(
            @PathVariable Long roomId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();
        List<ChatMessageDetail> allMessages = queryChatMessageDataUseCase.findAllMessagesInChatRoom(
                userId, roomId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ChatMessagesResponse(allMessages));
    }
}

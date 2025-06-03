package kr.co.pawong.pwbe.chat.adapter.in.api;

import java.util.List;
import kr.co.pawong.pwbe.chat.adapter.in.api.dto.response.ChatMessagesResponse;
import kr.co.pawong.pwbe.chat.application.port.in.QueryChatMessageDataUseCase;
import kr.co.pawong.pwbe.chat.application.port.in.dto.ChatMessageDetail;
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

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatMessageQueryController {

    private final QueryChatMessageDataUseCase queryChatMessageDataUseCase;

    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<ChatMessagesResponse> findAllMessagesInChatRoom(
            @PathVariable Long roomId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();
        List<ChatMessageDetail> allMessages = queryChatMessageDataUseCase.findAllMessagesInChatRoom(
                userId, roomId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ChatMessagesResponse(allMessages));
    }

    @GetMapping("/rooms/{roomId}/messages/lastest")
    public ResponseEntity<ChatMessagesResponse> findLastestMessagesInChatRoom(
            @PathVariable Long roomId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();
        List<ChatMessageDetail> lastestMessages = queryChatMessageDataUseCase.findLatestMessagesInChatRoom(userId, roomId, null);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ChatMessagesResponse(lastestMessages));
    }

    /**
     * 2) 과거 페이지 가져오기
     * → before 파라미터(“밀리초”)를 넘기면, before보다 이전 메시지 N건만 가져옴
     */
    @GetMapping("/rooms/{roomId}/messages/before")
    public ResponseEntity<ChatMessagesResponse> findMessagesBeforeInChatRoom(
            @PathVariable Long roomId,
            @RequestParam("before") Long beforeMillis,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();

        List<ChatMessageDetail> olderPage =
                queryChatMessageDataUseCase.findLatestMessagesInChatRoom(userId, roomId, beforeMillis);

        return ResponseEntity.ok(new ChatMessagesResponse(olderPage));
    }
}

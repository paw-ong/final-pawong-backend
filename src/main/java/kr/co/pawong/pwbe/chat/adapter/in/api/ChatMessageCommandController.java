package kr.co.pawong.pwbe.chat.adapter.in.api;

import kr.co.pawong.pwbe.chat.application.port.in.CommandChatMessageDataUseCase;
import kr.co.pawong.pwbe.user.adapter.out.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatMessageCommandController {

    private final CommandChatMessageDataUseCase commandChatMessageDataUseCase;

    @PatchMapping("/{roomId}/read")
    public ResponseEntity<Void> readChatMessages(
            @PathVariable Long roomId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        commandChatMessageDataUseCase.markAllAsRead(roomId, customUserDetails.getUserId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

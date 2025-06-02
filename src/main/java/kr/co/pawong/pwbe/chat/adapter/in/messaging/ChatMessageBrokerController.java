package kr.co.pawong.pwbe.chat.adapter.in.messaging;

import kr.co.pawong.pwbe.chat.adapter.in.messaging.dto.request.ChatMessageCreateRequest;
import kr.co.pawong.pwbe.chat.application.port.in.SendChatMessageBrokerUseCase;
import kr.co.pawong.pwbe.global.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatMessageBrokerController {

    private final SendChatMessageBrokerUseCase sendChatMessageBrokerUseCase;

    @MessageMapping("/chat.send/{roomId}")
    public void sendMessage(
            @Payload ChatMessageCreateRequest request,
            @DestinationVariable Long roomId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        sendChatMessageBrokerUseCase.createAndSendChatMessage(request, roomId, userDetails.getUserId());
    }

    @MessageMapping("/chat.read/{roomId}")
    public void markRead(
            @DestinationVariable Long roomId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        sendChatMessageBrokerUseCase.readMessage(roomId, userDetails.getUserId());
    }

}
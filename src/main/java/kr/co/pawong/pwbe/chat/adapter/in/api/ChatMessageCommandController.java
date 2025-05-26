package kr.co.pawong.pwbe.chat.adapter.in.api;

import kr.co.pawong.pwbe.chat.adapter.in.api.dto.request.ChatMessageCreateRequest;
import kr.co.pawong.pwbe.chat.adapter.in.api.dto.response.ChatMessageBrokerResponse;
import kr.co.pawong.pwbe.chat.application.port.in.CommandChatMessageDataUseCase;
import kr.co.pawong.pwbe.chat.application.port.in.dto.ChatMessageDetail;
import kr.co.pawong.pwbe.chat.enums.ChatMessageStatus;
import kr.co.pawong.pwbe.user.adapter.out.security.CustomUserDetails;
import kr.co.pawong.pwbe.user.application.port.in.QueryNicknameUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatMessageCommandController {

    private final CommandChatMessageDataUseCase commandChatMessageDataUseCase;
    private final QueryNicknameUseCase queryNicknameUseCase;

    @MessageMapping("/chat.send/{roomId}")
    @SendToUser("/queue/chat/{roomId}")
    public ChatMessageBrokerResponse sendMessage(
            @Payload ChatMessageCreateRequest request,
            @DestinationVariable Long roomId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long chatMessageId = commandChatMessageDataUseCase.createChatMessage(
                roomId,
                userDetails.getUserId(),
                request.getContent());
        return new ChatMessageBrokerResponse(
                new ChatMessageDetail(
                        chatMessageId,
                        request.getContent(),
                        userDetails.getUserId(),
                        ChatMessageStatus.SENT,
                        request.getCreatedAt()
                ));
    }
}
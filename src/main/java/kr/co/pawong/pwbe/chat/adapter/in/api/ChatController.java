package kr.co.pawong.pwbe.chat.adapter.in.api;

import kr.co.pawong.pwbe.chat.adapter.in.api.dto.request.ChatMessageCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    @MessageMapping("/chat.send/{roomId}")
    @SendToUser("/queue/chat/{roomId}")
    public ChatMessageCreateRequest sendMessage(@Payload ChatMessageCreateRequest messageDTO,
            @DestinationVariable Long roomId) {
        log.info(messageDTO.getSender());
        log.info(messageDTO.getContent());
        messageDTO.setRoomId(roomId);
        return messageDTO;
    }
}
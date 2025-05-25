package kr.co.pawong.pwbe.chat.adapter.in;

import kr.co.pawong.pwbe.chat.adapter.in.dto.ChatMessageDto;
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
    public ChatMessageDto sendMessage(@Payload ChatMessageDto messageDTO, @DestinationVariable Long roomId) {
        log.info(messageDTO.getSender());
        log.info(messageDTO.getContent());
        messageDTO.setRoomId(roomId);
        return messageDTO;
    }
}
package kr.co.pawong.pwbe.chat.application.listener.event;

import kr.co.pawong.pwbe.chat.domain.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatMessageCreatedEvent {
    private ChatMessage chatMessage;
}

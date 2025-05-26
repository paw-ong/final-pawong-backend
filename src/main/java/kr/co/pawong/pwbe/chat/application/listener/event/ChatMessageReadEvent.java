package kr.co.pawong.pwbe.chat.application.listener.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatMessageReadEvent {
    private final Long chatRoomId;
    private final Long readerId;
    private final Long lastReadMessageId;
}

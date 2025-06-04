package kr.co.pawong.pwbe.chat.application.listener.event;

import kr.co.pawong.pwbe.chat.domain.ChatMessage;
import kr.co.pawong.pwbe.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatNotificationEvent {
    User receiver;
    ChatMessage chatMessage;
}

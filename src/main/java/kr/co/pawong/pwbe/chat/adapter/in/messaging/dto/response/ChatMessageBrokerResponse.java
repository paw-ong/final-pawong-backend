package kr.co.pawong.pwbe.chat.adapter.in.messaging.dto.response;

import kr.co.pawong.pwbe.chat.application.port.in.dto.ChatMessageDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatMessageBrokerResponse {
    ChatMessageDetail chatMessageDetail;
}

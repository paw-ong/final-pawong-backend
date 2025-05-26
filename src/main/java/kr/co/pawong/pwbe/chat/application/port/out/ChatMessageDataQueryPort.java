package kr.co.pawong.pwbe.chat.application.port.out;

import java.util.List;
import kr.co.pawong.pwbe.chat.domain.ChatMessage;

public interface ChatMessageDataQueryPort {

    List<ChatMessage> findChatMessagesByChatRoomIdInLatestOrder(Long chatRoomId);
    ChatMessage findLatestReadMessageOrThrow(Long chatRoomId, Long userId);

}

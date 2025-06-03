package kr.co.pawong.pwbe.chat.application.port.out;

import java.util.List;
import kr.co.pawong.pwbe.chat.domain.ChatMessage;

public interface ChatMessageCachePort {

    void cacheMessage(Long chatRoomId, ChatMessage chatMessage, int cacheSize);
    List<ChatMessage> getLatestMessages(Long chatRoomId, int maxSize);

}

package kr.co.pawong.pwbe.chat.application.port.out;

import java.util.List;
import kr.co.pawong.pwbe.chat.domain.ChatMessage;

public interface ChatMessageCachePort {

    void cacheMessage(Long chatRoomId, ChatMessage chatMessage);
    void cacheMessageChunk(List<ChatMessage> recentChatMessage, Long roomId);
    List<ChatMessage> getLatestMessages(Long chatRoomId);
    void updateTotalCount(Long chatRoomId, Long count);
    Long getTotalCount(Long roomId);
}

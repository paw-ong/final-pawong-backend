package kr.co.pawong.pwbe.chat.application.port.out;

import java.time.Instant;
import java.util.List;
import kr.co.pawong.pwbe.chat.domain.ChatMessage;

public interface ChatMessageDataQueryPort {

    List<ChatMessage> findChatMessagesByChatRoomIdInLatestOrder(Long chatRoomId);
    ChatMessage findLatestChatMessageInChatRoomOrThrow(Long chatRoomId);
    ChatMessage findLatestReadMessageOrThrow(Long chatRoomId, Long userId);
    List<ChatMessage> findLatestNByChatRoom(Long chatRoomId, int N);
    List<ChatMessage> findByChatRoomIdAndCreatedAtBeforeOrderByCreatedAtDesc(Long chatRoomId, Instant oldestRedisCreatedAt, int N);
}

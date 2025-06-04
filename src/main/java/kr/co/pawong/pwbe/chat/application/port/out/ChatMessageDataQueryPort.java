package kr.co.pawong.pwbe.chat.application.port.out;

import java.util.List;
import kr.co.pawong.pwbe.chat.domain.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ChatMessageDataQueryPort {

    List<ChatMessage> findChatMessagesByChatRoomIdInLatestOrder(Long chatRoomId);
    ChatMessage findLatestChatMessageInChatRoomOrThrow(Long chatRoomId);
    ChatMessage findLatestReadMessageOrThrow(Long chatRoomId, Long userId);
    Slice<ChatMessage> findSliceMessages(Long chatRoomId, Pageable pageable);
    Long countByChatRoomId(Long chatRoomId);
}

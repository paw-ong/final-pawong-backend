package kr.co.pawong.pwbe.chat.application.port.in;

import java.util.List;
import kr.co.pawong.pwbe.chat.application.port.in.dto.ChatMessageDetail;

public interface QueryChatMessageDataUseCase {

    List<ChatMessageDetail> findAllMessagesInChatRoom(Long userId, Long chatRoomId);

    List<ChatMessageDetail> findLatestMessagesInChatRoom(Long userId, Long roomId, Long beforeMillis);
}

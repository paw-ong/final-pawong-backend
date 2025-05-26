package kr.co.pawong.pwbe.chat.application.port.out;

import java.util.List;
import kr.co.pawong.pwbe.chat.domain.ChatRoom;

public interface ChatRoomDataQueryPort {

    List<ChatRoom> findChatRoomsByUserId(Long userId);

    List<ChatRoom> findChatRoomsByPostId(Long postId);

    ChatRoom findChatRoomByIdOrThrow(Long chatRoomId);

    ChatRoom findChatRoomByParticipantIdAndPostIdOrThrow(Long participantId, Long postId);
}

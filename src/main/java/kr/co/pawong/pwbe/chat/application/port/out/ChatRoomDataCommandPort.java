package kr.co.pawong.pwbe.chat.application.port.out;

import kr.co.pawong.pwbe.chat.domain.ChatRoom;

public interface ChatRoomDataCommandPort {

    Long saveChatRoomOrThrow(ChatRoom chatRoom);

    boolean deactivateChatRoomOrThrow(Long chatRoomId);
}

package kr.co.pawong.pwbe.chat.application.port.out;

import kr.co.pawong.pwbe.chat.domain.ChatRoom;

// TODO implement param, return
public interface ChatRoomDataCommandPort {

    Long createChatRoom(ChatRoom chatRoom);
}

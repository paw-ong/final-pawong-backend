package kr.co.pawong.pwbe.chat.application.port.in;

import kr.co.pawong.pwbe.chat.domain.ChatMessage;

public interface CommandChatMessageDataUseCase {

    ChatMessage createChatMessage(Long chatRoomId, Long senderId, String content);
    void markAllAsRead(Long roomId, Long userId);
}

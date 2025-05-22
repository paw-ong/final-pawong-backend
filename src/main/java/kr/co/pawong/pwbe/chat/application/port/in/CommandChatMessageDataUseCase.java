package kr.co.pawong.pwbe.chat.application.port.in;

public interface CommandChatMessageDataUseCase {

    Long createChatMessage(Long chatRoomId, Long senderId, String content);
}

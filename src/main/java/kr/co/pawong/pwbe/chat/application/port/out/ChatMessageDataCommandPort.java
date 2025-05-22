package kr.co.pawong.pwbe.chat.application.port.out;

import kr.co.pawong.pwbe.chat.domain.ChatMessage;

public interface ChatMessageDataCommandPort {

    /**
     * @param chatMessage
     * @return 저장된 메시지의 id
     */
    Long saveChatMessage(ChatMessage chatMessage);

}

package kr.co.pawong.pwbe.chat.application.port.in;

import kr.co.pawong.pwbe.chat.adapter.in.messaging.dto.request.ChatMessageCreateRequest;

public interface SendChatMessageBrokerUseCase {

    void createAndSendChatMessage(ChatMessageCreateRequest request, Long chatRoomId, Long userId);

}

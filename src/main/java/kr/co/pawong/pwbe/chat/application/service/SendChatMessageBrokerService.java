package kr.co.pawong.pwbe.chat.application.service;

import kr.co.pawong.pwbe.chat.adapter.in.messaging.dto.request.ChatMessageCreateRequest;
import kr.co.pawong.pwbe.chat.application.listener.event.ChatMessageCreatedEvent;
import kr.co.pawong.pwbe.chat.application.port.in.CommandChatMessageDataUseCase;
import kr.co.pawong.pwbe.chat.application.port.in.SendChatMessageBrokerUseCase;
import kr.co.pawong.pwbe.chat.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SendChatMessageBrokerService implements SendChatMessageBrokerUseCase {

    private final CommandChatMessageDataUseCase commandChatMessageDataUseCase;
    private final ApplicationEventPublisher publisher;

    @Override
    @Transactional
    public void createAndSendChatMessage(ChatMessageCreateRequest request, Long chatRoomId,
            Long userId) {
        ChatMessage chatMessage = createChatMessage(request, chatRoomId, userId);
        publisher.publishEvent(new ChatMessageCreatedEvent(chatMessage));
    }

    private ChatMessage createChatMessage(ChatMessageCreateRequest request, Long chatRoomId,
            Long userId) {
        return commandChatMessageDataUseCase.createChatMessage(
                chatRoomId,
                userId,
                request.getContent());
    }
}

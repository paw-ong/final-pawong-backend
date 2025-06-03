package kr.co.pawong.pwbe.chat.application.service;

import kr.co.pawong.pwbe.chat.adapter.in.messaging.dto.request.ChatMessageCreateRequest;
import kr.co.pawong.pwbe.chat.application.listener.event.ChatMessageCreatedEvent;
import kr.co.pawong.pwbe.chat.application.listener.event.ChatMessageReadEvent;
import kr.co.pawong.pwbe.chat.application.port.in.CommandChatMessageDataUseCase;
import kr.co.pawong.pwbe.chat.application.port.in.SendChatMessageBrokerUseCase;
import kr.co.pawong.pwbe.chat.application.port.out.ChatMessageCachePort;
import kr.co.pawong.pwbe.chat.application.port.out.ChatMessageDataQueryPort;
import kr.co.pawong.pwbe.chat.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SendChatMessageBrokerService implements SendChatMessageBrokerUseCase {

    private static final int MAX_CACHE_SIZE = 100;

    private final CommandChatMessageDataUseCase commandChatMessageDataUseCase;
    private final ChatMessageDataQueryPort chatMessageDataQueryPort;
    private final ApplicationEventPublisher publisher;
    private final ChatMessageCachePort chatMessageCachePort;


    @Override
    @Transactional
    public void createAndSendChatMessage(ChatMessageCreateRequest request, Long chatRoomId,
            Long userId) {
        ChatMessage chatMessage = createChatMessage(request, chatRoomId, userId);
        chatMessageCachePort.cacheMessage(chatRoomId, chatMessage, MAX_CACHE_SIZE);
        publisher.publishEvent(new ChatMessageCreatedEvent(chatMessage));
    }

    @Override
    @Transactional
    public void readMessage(Long roomId, Long userId) {
        commandChatMessageDataUseCase.markAllAsRead(roomId, userId);
        ChatMessage chatMessage = chatMessageDataQueryPort.findLatestReadMessageOrThrow(roomId, userId);
        publisher.publishEvent(new ChatMessageReadEvent(roomId, userId, chatMessage.getMessageId()));
    }

    private ChatMessage createChatMessage(ChatMessageCreateRequest request, Long chatRoomId,
            Long userId) {
        return commandChatMessageDataUseCase.createChatMessage(
                chatRoomId,
                userId,
                request.getContent());
    }
}

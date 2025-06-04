package kr.co.pawong.pwbe.chat.application.service;

import kr.co.pawong.pwbe.chat.adapter.in.messaging.dto.request.ChatMessageCreateRequest;
import kr.co.pawong.pwbe.chat.adapter.out.notification.ChatNotificationAdapter;
import kr.co.pawong.pwbe.chat.application.listener.event.ChatMessageCreatedEvent;
import kr.co.pawong.pwbe.chat.application.listener.event.ChatMessageReadEvent;
import kr.co.pawong.pwbe.chat.application.port.in.CommandChatMessageDataUseCase;
import kr.co.pawong.pwbe.chat.application.port.in.SendChatMessageBrokerUseCase;
import kr.co.pawong.pwbe.chat.application.port.out.ChatMessageBrokerPort;
import kr.co.pawong.pwbe.chat.application.port.out.ChatMessageDataQueryPort;
import kr.co.pawong.pwbe.chat.application.port.out.ChatRoomDataQueryPort;
import kr.co.pawong.pwbe.chat.domain.ChatMessage;
import kr.co.pawong.pwbe.chat.domain.ChatRoom;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import kr.co.pawong.pwbe.notification.application.port.in.dto.NotificationRequest;
import kr.co.pawong.pwbe.user.application.port.out.UserDataQueryPort;
import kr.co.pawong.pwbe.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SendChatMessageBrokerService implements SendChatMessageBrokerUseCase {

    private final CommandChatMessageDataUseCase commandChatMessageDataUseCase;
    private final ChatMessageDataQueryPort chatMessageDataQueryPort;
    private final ChatRoomDataQueryPort chatRoomDataQueryPort;
    private final UserDataQueryPort userDataQueryPort;
    private final ApplicationEventPublisher publisher;

    @Override
    @Transactional
    public void createAndSendChatMessage(ChatMessageCreateRequest request, Long chatRoomId,
            Long userId) {
        /* save to db */
        ChatMessage chatMessage = createChatMessage(request, chatRoomId, userId);

        /* find user in chat room */
        ChatRoom chatRoom = chatRoomDataQueryPort.findChatRoomByIdOrThrow(chatMessage.getChatRoomId());
        User author = userDataQueryPort.findByUserIdOrThrow(chatRoom.getAuthorId());
        User participant = userDataQueryPort.findByUserIdOrThrow(chatRoom.getParticipantId());


        /* Send a chat message */
        publisher.publishEvent(new ChatMessageCreatedEvent(chatMessage, author, participant));
    }

    @Override
    @Transactional
    public void readMessage(Long roomId, Long userId) {
        commandChatMessageDataUseCase.markAllAsRead(roomId, userId);
        ChatMessage chatMessage = chatMessageDataQueryPort.findLatestReadMessageOrThrow(roomId,
                userId);
        publisher.publishEvent(
                new ChatMessageReadEvent(roomId, userId, chatMessage.getMessageId()));
    }

    private ChatMessage createChatMessage(ChatMessageCreateRequest request, Long chatRoomId,
            Long userId) {
        return commandChatMessageDataUseCase.createChatMessage(
                chatRoomId,
                userId,
                request.getContent());
    }
}

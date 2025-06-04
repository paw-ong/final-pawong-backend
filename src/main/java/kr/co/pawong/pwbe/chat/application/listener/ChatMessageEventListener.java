package kr.co.pawong.pwbe.chat.application.listener;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.*;

import java.util.Map;
import java.util.Objects;
import kr.co.pawong.pwbe.chat.application.listener.event.ChatMessageCreatedEvent;
import kr.co.pawong.pwbe.chat.application.listener.event.ChatMessageReadEvent;
import kr.co.pawong.pwbe.chat.application.listener.event.ChatNotificationEvent;
import kr.co.pawong.pwbe.chat.application.port.in.dto.ChatMessageDetail;
import kr.co.pawong.pwbe.chat.application.port.out.ChatMessageBrokerPort;
import kr.co.pawong.pwbe.chat.application.port.out.ChatRoomDataQueryPort;
import kr.co.pawong.pwbe.chat.domain.ChatMessage;
import kr.co.pawong.pwbe.chat.domain.ChatRoom;
import kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import kr.co.pawong.pwbe.user.application.port.out.UserDataQueryPort;
import kr.co.pawong.pwbe.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMessageEventListener {

    private final ChatRoomDataQueryPort chatRoomDataQueryPort;
    private final UserDataQueryPort userDataQueryPort;
    private final ChatMessageBrokerPort chatMessageBrokerPort;
    private final ApplicationEventPublisher publisher;

    @Async("chatExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onChatMessageCreated(ChatMessageCreatedEvent event) {

        try {
            ChatMessage chatMessage = event.getChatMessage();
            User sender = userDataQueryPort.findByUserIdOrThrow(
                    event.getChatMessage().getSenderId());
            ChatMessageDetail messageDetail = ChatMessageDetail
                    .from(chatMessage)
                    .updateSenderInfo(
                            sender.getNickname(),
                            sender.getProfileImage());

            /* send it to users */
            String userDest = "/queue/chat/" + chatMessage.getChatRoomId();
            chatMessageBrokerPort.sendMessageToUser(String.valueOf(event.getAuthor().getSocialId()),
                    userDest, messageDetail);
            chatMessageBrokerPort.sendMessageToUser(
                    String.valueOf(event.getParticipant().getSocialId()), userDest, messageDetail);

            /* send notification */
            User receiver = (event.getAuthor().getUserId().equals(chatMessage.getSenderId())) ? event.getParticipant() : event.getAuthor();
            publisher.publishEvent(new ChatNotificationEvent(receiver, chatMessage));

        } catch (Exception exception) {
            log.info(exception.getMessage());
            throw new BaseException(BROKER_FAIL);
        }
    }

    @Async("chatExecutor")
    @EventListener
    public void onChatMessageRead(ChatMessageReadEvent event) {

        /* find recipient in chat room */
        ChatRoom chatRoom = chatRoomDataQueryPort.findChatRoomByIdOrThrow(event.getChatRoomId());
        Long recipientId = Objects.equals(chatRoom.getAuthorId(), event.getReaderId()) ? chatRoom.getParticipantId() : chatRoom.getAuthorId();
        User recipient = userDataQueryPort.findByUserIdOrThrow(recipientId);

        /* send it to the recipient */
        String userDest = "/queue/read-receipts/" + event.getChatRoomId();
        chatMessageBrokerPort.sendMessageToUser(String.valueOf(recipient.getSocialId()), userDest, Map.of("readerId", event.getReaderId(), "lastReadMessageId", event.getLastReadMessageId())
        );
    }

}

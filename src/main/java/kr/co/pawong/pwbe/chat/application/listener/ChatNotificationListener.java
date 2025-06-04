package kr.co.pawong.pwbe.chat.application.listener;

import kr.co.pawong.pwbe.chat.application.listener.event.ChatNotificationEvent;
import kr.co.pawong.pwbe.chat.application.port.out.ChatMessageBrokerPort;
import kr.co.pawong.pwbe.chat.application.port.out.ChatRoomDataQueryPort;
import kr.co.pawong.pwbe.chat.domain.ChatMessage;
import kr.co.pawong.pwbe.chat.domain.ChatRoom;
import kr.co.pawong.pwbe.notification.application.port.in.dto.NotificationRequest;
import kr.co.pawong.pwbe.notification.application.port.out.ChatNotificationPort;
import kr.co.pawong.pwbe.notification.enums.TargetType;
import kr.co.pawong.pwbe.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatNotificationListener {

    private final ChatMessageBrokerPort chatMessageBrokerPort;
    private final ChatNotificationPort chatNotificationPort;
    private final ChatRoomDataQueryPort chatRoomDataQueryPort;

    @Async("notificationExecutor")
    @EventListener
    public void onChatMessageCreated(ChatNotificationEvent event) {

        /* Send notification */
        sendChatNotificationToSessionUser(event.getReceiver(), event.getChatMessage());

    }

    private void sendChatNotificationToSessionUser(User receiver, ChatMessage chatMessage) {
        if (chatMessageBrokerPort.isUserSubscribedToRoom(receiver, chatMessage.getChatRoomId())) {
            return;
        }
        ChatRoom chatRoom = chatRoomDataQueryPort.findChatRoomByIdOrThrow(
                chatMessage.getChatRoomId());

        chatNotificationPort.sendChatFcmNotification(new NotificationRequest(
                receiver.getUserId(),
                chatMessage.getContent(),
                chatMessage.getChatRoomId(),
                TargetType.CHAT,
                chatRoom.getPostId()
        ));
    }

}

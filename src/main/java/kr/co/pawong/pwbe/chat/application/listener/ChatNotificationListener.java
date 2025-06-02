package kr.co.pawong.pwbe.chat.application.listener;

import kr.co.pawong.pwbe.chat.application.listener.event.ChatNotificationEvent;
import kr.co.pawong.pwbe.chat.application.port.out.ChatMessageBrokerPort;
import kr.co.pawong.pwbe.chat.domain.ChatMessage;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import kr.co.pawong.pwbe.notification.application.port.in.dto.NotificationRequest;
import kr.co.pawong.pwbe.notification.application.port.out.ChatNotificationPort;
import kr.co.pawong.pwbe.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatNotificationListener {

    private final ChatMessageBrokerPort chatMessageBrokerPort;
    private final ChatNotificationPort chatNotificationPort;

    @Async("notificationExecutor")   // 별도 스레드풀에서 실행
    @EventListener
    public void onChatMessageCreated(ChatNotificationEvent event) {

        /* Send notification */
        sendChatNotificationToSessionUser(event.getReceiver(), event.getChatMessage());

    }

    private void sendChatNotificationToSessionUser(User receiver, ChatMessage chatMessage) {
        if (chatMessageBrokerPort.isUserSubscribedToRoom(receiver, chatMessage.getChatRoomId())) {
            return;
        }

        chatNotificationPort.sendChatFcmNotification(new NotificationRequest(
                receiver.getUserId(),
                chatMessage.getContent(),
                chatMessage.getChatRoomId(),
                PostType.LOST
        ));
    }

}

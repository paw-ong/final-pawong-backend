package kr.co.pawong.pwbe.chat.adapter.out.notification;

import kr.co.pawong.pwbe.notification.application.port.in.NotificationUseCase;
import kr.co.pawong.pwbe.notification.application.port.in.dto.NotificationRequest;
import kr.co.pawong.pwbe.notification.application.port.out.ChatNotificationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatNotificationAdapter implements ChatNotificationPort {

    private final NotificationUseCase notificationUseCase;

    @Override
    public void sendChatFcmNotification(NotificationRequest notificationRequest) {
        notificationUseCase.sendChatNotification(notificationRequest);
    }
}

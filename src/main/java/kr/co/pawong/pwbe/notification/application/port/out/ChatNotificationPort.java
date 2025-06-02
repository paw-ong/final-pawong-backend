package kr.co.pawong.pwbe.notification.application.port.out;

import kr.co.pawong.pwbe.notification.application.port.in.dto.NotificationRequest;

public interface ChatNotificationPort {

    void sendChatFcmNotification(NotificationRequest notificationRequest);

}

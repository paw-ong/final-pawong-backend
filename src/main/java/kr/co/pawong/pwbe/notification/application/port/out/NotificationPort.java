package kr.co.pawong.pwbe.notification.application.port.out;

import kr.co.pawong.pwbe.notification.domain.Notification;

public interface NotificationPort {
    Notification save(Notification notification);
    Notification findById(Long id);
}

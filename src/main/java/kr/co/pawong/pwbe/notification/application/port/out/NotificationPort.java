package kr.co.pawong.pwbe.notification.application.port.out;

import java.util.Optional;
import kr.co.pawong.pwbe.notification.domain.Notification;

public interface NotificationPort {
    Notification save(Notification notification);
    Optional<Notification> findById(Long id);
}

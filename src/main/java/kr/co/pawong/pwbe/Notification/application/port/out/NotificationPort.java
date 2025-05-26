package kr.co.pawong.pwbe.Notification.application.port.out;

import java.util.Optional;
import kr.co.pawong.pwbe.Notification.domain.Notification;

public interface NotificationPort {
    Notification save(Notification notification);
    Optional<Notification> findById(Long id);
}

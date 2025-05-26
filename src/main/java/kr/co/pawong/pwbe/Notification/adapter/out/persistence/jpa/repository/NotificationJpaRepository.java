package kr.co.pawong.pwbe.Notification.adapter.out.persistence.jpa.repository;

import kr.co.pawong.pwbe.Notification.adapter.out.persistence.jpa.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, Long> {
}

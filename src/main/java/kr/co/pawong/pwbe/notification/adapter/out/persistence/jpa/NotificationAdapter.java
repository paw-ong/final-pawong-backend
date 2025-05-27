package kr.co.pawong.pwbe.notification.adapter.out.persistence.jpa;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.NOTIFICATION_FIND_ERROR;
import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.NOTIFICATION_SAVE_ERROR;

import kr.co.pawong.pwbe.global.error.exception.BaseException;
import kr.co.pawong.pwbe.notification.adapter.out.persistence.jpa.entity.NotificationEntity;
import kr.co.pawong.pwbe.notification.adapter.out.persistence.jpa.repository.NotificationJpaRepository;
import kr.co.pawong.pwbe.notification.application.port.out.NotificationPort;
import kr.co.pawong.pwbe.notification.domain.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class NotificationAdapter implements NotificationPort {

    private final NotificationJpaRepository notificationJpaRepository;

    @Override
    public Notification save(Notification notification) {
        log.debug("알림 저장 시작: id={}, title={}", notification.getId(), notification.getTitle());

        try {
            // Notification -> NotificationEntity
            NotificationEntity entity = NotificationEntity.from(notification);

            // DB에 저장
            NotificationEntity saved = notificationJpaRepository.save(entity);

            // DB에 저장된 NotificationEntity -> Notification
            Notification savedNotification = saved.toModel();

            log.info("알림 저장 완료: id={}", savedNotification.getId());

            return savedNotification;
        } catch (Exception e) {
            log.error("알림 저장 실패: id={}", notification.getId(), e);
            throw new BaseException(NOTIFICATION_SAVE_ERROR);
        }
    }

    @Override
    public Notification findById(Long id) {
        try {
            // 엔티티 조회 및 Notification 변환
            return notificationJpaRepository.findById(id)
                    .map(NotificationEntity::toModel)
                    .orElse(null);
        } catch (Exception e) {
            log.error("알림 조회 실패: id={}", id, e);
            throw new BaseException(NOTIFICATION_FIND_ERROR);
        }
    }
}

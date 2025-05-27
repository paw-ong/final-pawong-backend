package kr.co.pawong.pwbe.notification.adapter.out.persistence.jpa;

import java.util.Optional;
import kr.co.pawong.pwbe.notification.adapter.out.persistence.jpa.entity.NotificationEntity;
import kr.co.pawong.pwbe.notification.adapter.out.persistence.jpa.repository.NotificationJpaRepository;
import kr.co.pawong.pwbe.notification.application.port.out.NotificationPort;
import kr.co.pawong.pwbe.notification.domain.Notification;
import kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
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
            throw new BaseException(CustomErrorCode.NOTIFICATION_SAVE_ERROR);
        }
    }

    @Override
    public Optional<Notification> findById(Long id) {
        log.debug("알림 조회 시작: id={}", id);

        try {
            // 엔티티 조회 및 Notification 변환
            Optional<Notification> notification = notificationJpaRepository.findById(id)
                    .map(NotificationEntity::toModel);

            if (notification.isPresent()) {
                log.debug("알림 조회 성공: id={}", id);
            } else {
                log.debug("알림을 찾을 수 없음: id={}", id);
            }

            return notification;
        } catch (Exception e) {
            log.error("알림 조회 실패: id={}", id, e);
            throw new BaseException(CustomErrorCode.NOTIFICATION_FIND_ERROR);
        }
    }
}

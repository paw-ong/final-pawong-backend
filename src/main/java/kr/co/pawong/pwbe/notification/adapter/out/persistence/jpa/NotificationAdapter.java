package kr.co.pawong.pwbe.notification.adapter.out.persistence.jpa;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.NOTIFICATION_FIND_ERROR;
import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.NOTIFICATION_SAVE_ERROR;
import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.NOTIFICATION_SEND_ERROR;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import kr.co.pawong.pwbe.notification.adapter.out.persistence.jpa.entity.NotificationEntity;
import kr.co.pawong.pwbe.notification.adapter.out.persistence.jpa.repository.NotificationJpaRepository;
import kr.co.pawong.pwbe.notification.application.port.out.NotificationPort;
import kr.co.pawong.pwbe.notification.application.service.dto.NotificationDto;
import kr.co.pawong.pwbe.notification.domain.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class NotificationAdapter implements NotificationPort {

    private final NotificationJpaRepository notificationJpaRepository;
    private final FirebaseMessaging firebaseMessaging;

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

    @Override
    public void sendFcmNotification(NotificationDto notificationDto) {
        try {
            log.debug("FCM 알림 전송 시작: title={}", notificationDto.getTitle());

            Message message = Message.builder()
                    .setToken(notificationDto.getToken())
                    .setWebpushConfig(WebpushConfig.builder() // WebPushConfig -> 웹 브라우저 알림 최적화
                            .putHeader("ttl", "300") // Time To Live: 5분
                            .setNotification(new WebpushNotification(
                                    notificationDto.getTitle(),
                                    notificationDto.getMessage()
                            ))
                            .build())
                    .putData("id", String.valueOf(notificationDto.getId()))
                    .putData("targetId", String.valueOf(notificationDto.getTargetId()))
                    .putData("targetType", String.valueOf(notificationDto.getTargetType()))
                    .putData("type", notificationDto.getType().name())
                    .putData("timeStamp", String.valueOf(System.currentTimeMillis()))
                    .build();

            // FCM 서버로 메시지 전송
            String response = firebaseMessaging.send(message);

            log.info("FCM 알림 전송 성공: response={}, title={}",
                    response, notificationDto.getTitle());

        } catch (FirebaseMessagingException e) {
            log.error("FCM 알림 전송 실패: notificationId={}, error={}",
                    notificationDto.getId(), e.getMessage(), e);
            throw new BaseException(NOTIFICATION_SEND_ERROR);
        } catch (Exception e) {
            log.error("알림 처리 중 예상치 못한 오류 발생: notificationId={}",
                    notificationDto.getId(), e);
            throw e;
        }
    }
}

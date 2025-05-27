package kr.co.pawong.pwbe.notification.application.service;

import java.util.Optional;
import kr.co.pawong.pwbe.notification.application.port.in.NotificationUseCase;
import kr.co.pawong.pwbe.notification.application.port.in.dto.NotificationRequest;
import kr.co.pawong.pwbe.notification.application.port.out.NotificationPort;
import kr.co.pawong.pwbe.notification.domain.Notification;
import kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import kr.co.pawong.pwbe.infrastructure.fcm.application.port.in.FcmUsecase;
import kr.co.pawong.pwbe.notification.application.service.dto.NotificationDto;
import kr.co.pawong.pwbe.infrastructure.messaging.application.port.in.PublishMessageUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService implements NotificationUseCase {

    private final PublishMessageUseCase publishMessageUseCase;
    private final FcmUsecase fcmUsecase;
    private final NotificationPort notificationPort;

    // 채팅 알림
    @Override
    public void sendChatNotification(NotificationRequest request) {
        log.info("채팅 알림 발송 시작: userId={}, targetId={}", request.getUserId(), request.getTargetId());

        try {
            // 1. userId로 유효한 FCM 토큰 조회
            String token = fcmUsecase.getValidTokenByUserId(request.getUserId());
            if (token == null) {
                throw new BaseException(CustomErrorCode.NOTIFICATION_INVALID_TOKEN);
            }

            // Notification 생성
            Notification notification = Notification.createChatNotification(
                    request.getUserId(),
                    token,
                    request.getMessage(),
                    request.getTargetId()
            );

            // DB에 저장
            Notification savedNotification = notificationPort.save(notification);
            log.debug("채팅 알림 저장 완료: id={}", savedNotification.getId());

            // NotificationDto로 변환하여 Kafka에 발행
            NotificationDto notificationDto = savedNotification.toDto();
            publishMessageUseCase.publishFcmNotificationMessage(notificationDto);

            log.info("채팅 알림 발송 완료: userId={}, id={}", request.getUserId(), savedNotification.getId());

        } catch (BaseException e) {
            log.error("채팅 알림 데이터 검증 실패: userId={}, error={}", request.getUserId(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("채팅 알림 발송 실패: userId={}, adoptionId={}", request.getUserId(), request.getTargetId(), e);
            throw new BaseException(CustomErrorCode.NOTIFICATION_CHAT_SEND_ERROR);
        }
    }

    // 유사 공고 알림
    @Override
    public void sendSimilarAdoptionNotification(NotificationRequest request) {
        log.info("유사 공고 알림 발송 시작: userId={}, adoptionId={}", request.getUserId(), request.getTargetId());

        try {
            // 1. userId로 유효한 FCM 토큰 조회
            String token = fcmUsecase.getValidTokenByUserId(request.getUserId());
            if (token == null) {
                throw new BaseException(CustomErrorCode.NOTIFICATION_INVALID_TOKEN);
            }

            // Notification 생성
            Notification notification = Notification.createSimilarAdoptionNotification(
                    request.getUserId(),
                    token,
                    request.getTargetId()
            );

            // DB에 저장
            Notification savedNotification = notificationPort.save(notification);
            log.debug("유사 공고 알림 저장 완료: id={}", savedNotification.getId());

            // NotificationDto로 변환하여 Kafka에 발행
            NotificationDto notificationDto = savedNotification.toDto();
            publishMessageUseCase.publishFcmNotificationMessage(notificationDto);

            log.info("유사 공고 알림 발송 완료: userId={}, id={}", request.getUserId(), savedNotification.getId());
        } catch (BaseException e) {
            // 도메인 검증 실패 또는 토큰 검증 실패
            log.error("유사 공고 알림 데이터 검증 실패: userId={}, error={}", request.getUserId(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("유사 공고 알림 발송 실패: userId={}, adoptionId={}", request.getUserId(), request.getTargetId(), e);
            throw new BaseException(CustomErrorCode.NOTIFICATION_ADOPTION_SEND_ERROR);
        }
    }

    // 알림 조회
    @Override
    public Optional<NotificationDto> getNotification(Long notificationId) {
        log.debug("알림 조회: id={}", notificationId);

        try {
            return notificationPort.findById(notificationId)
                    .map(Notification::toDto);
        } catch (Exception e) {
            log.error("알림 조회 실패: id={}", notificationId, e);
            throw new BaseException(CustomErrorCode.NOTIFICATION_NOT_FOUND);
        }
    }
}

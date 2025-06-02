package kr.co.pawong.pwbe.notification.application.service;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.FCM_INVALID_JSON_FORMAT;
import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.FCM_NOTIFICATION_MESSAGE_MISSING;
import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.FCM_NOTIFICATION_TITLE_MISSING;
import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.FCM_TOKEN_MISSING;
import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.NOTIFICATION_ADOPTION_SEND_ERROR;
import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.NOTIFICATION_CHAT_SEND_ERROR;
import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.NOTIFICATION_INVALID_TOKEN;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import kr.co.pawong.pwbe.infrastructure.fcm.application.port.in.FcmUsecase;
import kr.co.pawong.pwbe.infrastructure.messaging.application.port.in.PublishMessageUseCase;
import kr.co.pawong.pwbe.notification.application.port.in.NotificationUseCase;
import kr.co.pawong.pwbe.notification.application.port.in.dto.NotificationRequest;
import kr.co.pawong.pwbe.notification.application.port.out.NotificationPort;
import kr.co.pawong.pwbe.notification.application.service.dto.NotificationDto;
import kr.co.pawong.pwbe.notification.domain.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService implements NotificationUseCase {

    private final PublishMessageUseCase publishMessageUseCase;
    private final FcmUsecase fcmUsecase;
    private final NotificationPort notificationPort;
    private final ObjectMapper objectMapper;

    // 알림 메시지를 발행할 kafka 토픽 이름
    @Value("${kafka.topic.similar-animal-notification}")
    private String similarNotificationTopic;

    // 알림 메시지를 발행할 kafka 토픽 이름
    @Value("${kafka.topic.chat-notification}")
    private String chatNotificationTopic;


    // 채팅 알림
    @Override
    public void sendChatNotification(NotificationRequest request) {
        log.info("채팅 알림 발송 시작: userId={}, targetId={}", request.getUserId(), request.getTargetId());

        try {
            // 1. userId로 유효한 FCM 토큰 조회
            String token = fcmUsecase.getValidTokenByUserId(request.getUserId());
            if (token == null) {
                throw new BaseException(NOTIFICATION_INVALID_TOKEN);
            }

            // Notification 생성
            Notification notification = Notification.createChatNotification(
                    request.getUserId(),
                    request.getMessage(),
                    request.getTargetId()
            );

            // DB에 저장
            Notification savedNotification = notificationPort.save(notification);
            log.debug("채팅 알림 저장 완료: id={}", savedNotification.getId());

            // NotificationDto로 변환하여 Kafka에 발행
            NotificationDto notificationDto = savedNotification.toDto(token);
            publishMessageUseCase.publishMessage(chatNotificationTopic, notificationDto);

            log.info("채팅 알림 발송 완료: userId={}, id={}", request.getUserId(), savedNotification.getId());

        } catch (BaseException e) {
            log.error("채팅 알림 데이터 검증 실패: userId={}, error={}", request.getUserId(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("채팅 알림 발송 실패: userId={}, adoptionId={}", request.getUserId(), request.getTargetId(), e);
            throw new BaseException(NOTIFICATION_CHAT_SEND_ERROR);
        }
    }

    // 유사 공고 알림
    @Override
    public void sendSimilarAdoptionNotification(NotificationRequest request) {
        log.info("유사 공고 알림 발송 시작: userId={}, adoptionId={}", request.getUserId(), request.getTargetId());

        try {
            // 1. userId로 유효한 FCM 토큰 조회
            String token = fcmUsecase.getValidTokenByUserId(request.getUserId());
            if (token == null)
                throw new BaseException(NOTIFICATION_INVALID_TOKEN);

            // Notification 생성
            Notification notification = Notification.createSimilarAdoptionNotification(
                    request.getUserId(),
                    request.getTargetId(),
                    request.getPostType()
            );

            // DB에 저장
            Notification savedNotification = notificationPort.save(notification);
            log.debug("유사 공고 알림 저장 완료: id={}", savedNotification.getId());

            // NotificationDto로 변환하여 Kafka에 발행
            NotificationDto notificationDto = savedNotification.toDto(token);
            publishMessageUseCase.publishMessage(similarNotificationTopic, notificationDto);

            log.info("유사 공고 알림 발송 완료: userId={}, id={}", request.getUserId(), savedNotification.getId());
        } catch (BaseException e) {
            // 도메인 검증 실패 또는 토큰 검증 실패
            log.error("유사 공고 알림 데이터 검증 실패: userId={}, error={}", request.getUserId(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("유사 공고 알림 발송 실패: userId={}, adoptionId={}", request.getUserId(), request.getTargetId(), e);
            throw new BaseException(NOTIFICATION_ADOPTION_SEND_ERROR);
        }
    }

    @Override
    public void processFcmNotificationMessage(String jsonString) {
        try {
            // 1. JSON 파싱
            NotificationDto notificationDto = parseJsonToDto(jsonString);

            // 2. FCM 전송
            notificationPort.sendFcmNotification(notificationDto);

        } catch (JsonProcessingException e) {
            log.error("JSON 파싱 실패: jsonString={}, error={}", jsonString, e.getMessage(), e);
            throw new BaseException(FCM_INVALID_JSON_FORMAT);
        } catch (Exception e) {
            log.error("알림 처리 실패", e);
            throw e;
        }
    }

    // Json 파싱
    private NotificationDto parseJsonToDto(String jsonString) throws JsonProcessingException {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            throw new BaseException(FCM_INVALID_JSON_FORMAT);
        }

        try {
            NotificationDto notificationDto = objectMapper.readValue(jsonString, NotificationDto.class);

            // DTO 유효성 검증
            validateDto(notificationDto);

            return notificationDto;
        } catch (JsonProcessingException e) {
            log.error("JSON 파싱 실패 - 잘못된 JSON 형식: {}", jsonString, e);
            throw new BaseException(FCM_INVALID_JSON_FORMAT);
        }
    }

    // 필수 필드 유효성 검증
    private void validateDto(NotificationDto notificationDto) {
        if (notificationDto == null) {
            throw new BaseException(FCM_INVALID_JSON_FORMAT);
        }
        if (notificationDto.getToken() == null || notificationDto.getToken().trim().isEmpty()) {
            throw new BaseException(FCM_TOKEN_MISSING);
        }
        if (notificationDto.getTitle() == null || notificationDto.getTitle().trim().isEmpty()) {
            throw new BaseException(FCM_NOTIFICATION_TITLE_MISSING);
        }
        if (notificationDto.getMessage() == null || notificationDto.getMessage().trim().isEmpty()) {
            throw new BaseException(FCM_NOTIFICATION_MESSAGE_MISSING);
        }
    }

    // 알림 조회
    @Override
    public Notification getNotification(Long notificationId) {
        return notificationPort.findById(notificationId);
    }
}

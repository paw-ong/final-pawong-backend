package kr.co.pawong.pwbe.infrastructure.messaging.adapter.out;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.FCM_INVALID_JSON_FORMAT;
import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.FCM_NOTIFICATION_MESSAGE_MISSING;
import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.FCM_NOTIFICATION_TITLE_MISSING;
import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.FCM_TOKEN_MISSING;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import kr.co.pawong.pwbe.notification.application.service.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMessageConsumer {

    private final FirebaseMessaging firebaseMessaging;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.topic.notification}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeFcmNotificationMessage(String jsonString) {
        try {
            // 1. Json 파싱 및 검증
            NotificationDto notificationDto = parseJsonToDto(jsonString);

            // 2. FCM 메시지 구성 및 전송
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
                    .putData("adoptionId", String.valueOf(notificationDto.getTargetId()))
                    .putData("type", notificationDto.getType().name())
                    .putData("timeStamp", String.valueOf(System.currentTimeMillis()))
                    .build();

            // FCM 서버로 메시지 전송
            String response = firebaseMessaging.send(message);

            log.info("FCM 알림 전송 성공: response={}, title={}",
                    response, notificationDto.getTitle());
            log.info("알림 처리 완료: id={}", notificationDto.getId());
        } catch (JsonProcessingException e) {
            // JSON 파싱 실패 처리 - 메시지 형식 오류
            log.error("JSON 파싱 실패: jsonString={}, error={}", jsonString, e.getMessage(), e);
        } catch (FirebaseMessagingException e) {
            // FCM 특정 예외 처리 (토큰 만료, 잘못된 토큰 등)
            log.error("FCM 알림 전송 실패: jsonString={}, error={}", jsonString, e.getMessage(), e);
        } catch (Exception e) {
            // 기타 예상치 못한 예외 처리
            log.error("알림 처리 중 예상치 못한 오류 발생: jsonString={}", jsonString, e);
        }
    }

    // Json 문자열을 NotificationDto로 파싱
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
}

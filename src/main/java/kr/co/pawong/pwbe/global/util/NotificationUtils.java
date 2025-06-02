package kr.co.pawong.pwbe.global.util;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.EMAIL_INVALID_JSON_FORMAT;
import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.FCM_INVALID_JSON_FORMAT;
import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.FCM_NOTIFICATION_MESSAGE_MISSING;
import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.FCM_NOTIFICATION_TITLE_MISSING;
import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.FCM_TOKEN_MISSING;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import kr.co.pawong.pwbe.notification.application.service.dto.NotificationDto;
import kr.co.pawong.pwbe.notification.application.service.dto.NotificationEmailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationUtils {

    private final ObjectMapper objectMapper;

    // Json 파싱
    public NotificationDto parseJsonToDto(String jsonString) throws JsonProcessingException {
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

    // Json 파싱
    public NotificationEmailDto parseJsonToEmailDto(String jsonString) throws JsonProcessingException {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            throw new BaseException(FCM_INVALID_JSON_FORMAT);
        }

        try {
            NotificationEmailDto notificationEmailDto = objectMapper.readValue(jsonString, NotificationEmailDto.class);

            return notificationEmailDto;
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

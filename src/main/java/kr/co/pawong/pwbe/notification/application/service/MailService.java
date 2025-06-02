package kr.co.pawong.pwbe.notification.application.service;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.EMAIL_DUPLICATE;
import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.EMAIL_INVALID_JSON_FORMAT;
import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.EMAIL_SEND_FAIL;
import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.REDIS_SAVE_ERROR;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.Duration;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import kr.co.pawong.pwbe.global.util.CodeGenerator;
import kr.co.pawong.pwbe.global.util.NotificationUtils;
import kr.co.pawong.pwbe.global.util.RedisUtils;
import kr.co.pawong.pwbe.infrastructure.messaging.application.port.in.PublishMessageUseCase;
import kr.co.pawong.pwbe.notification.application.port.in.CustomMailSenderUseCase;
import kr.co.pawong.pwbe.notification.application.port.in.MailUseCase;
import kr.co.pawong.pwbe.notification.application.port.in.dto.NotificationRequest;
import kr.co.pawong.pwbe.notification.application.service.dto.NotificationEmailDto;
import kr.co.pawong.pwbe.notification.domain.Notification;
import kr.co.pawong.pwbe.user.application.port.in.QueryUserDataUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService implements MailUseCase {

    private static final String AUTH_CODE_PREFIX = "AuthCode ";
    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;
    private final CustomMailSenderUseCase customMailSenderUseCase;
    private final RedisUtils redisUtils;
    private final NotificationUtils notificationUtils;
    private final QueryUserDataUseCase queryUserDataUseCase;
    private final PublishMessageUseCase publishMessageUseCase;


    // 알림 메시지를 발행할 kafka 토픽 이름
    @Value("${kafka.topic.mail-notification}")
    private String mailNotificationTopic;

    // 이메일 인증
    @Override
    public void sendCodeToEmail(String toEmail) {
        this.checkDuplicatedEmail(toEmail);
        String title = "PAWONG 회원가입 이메일 인증 번호";
        String authCode = CodeGenerator.generateCode();
        String redisKey = AUTH_CODE_PREFIX + toEmail;

        // Redis에 저장
        try {
            redisUtils.setDataExpire(
                    redisKey,
                    authCode,
                    Duration.ofMillis(this.authCodeExpirationMillis)
            );
        } catch (Exception e) {
            // Redis 저장 실패 시 예외 던지고 메서드 종료
            throw new BaseException(REDIS_SAVE_ERROR);
        }

        // 이메일 전송 시도
        try {
            customMailSenderUseCase.sendCodeEmail(toEmail, title, authCode);
        } catch (Exception e) {
            // 이메일 전송 실패 시 Redis에서 방금 저장한 키 삭제
            redisUtils.deleteData(redisKey);
            throw new BaseException(EMAIL_SEND_FAIL);
        }
    }

    // 유사 공고 이메일
    @Override
    public void sendSimilarAdoptionEmail(NotificationRequest request){
        try {
            // Notification 생성
            Notification notification = Notification.createSimilarAdoptionMailNotification(
                    request.getUserId(),
                    request.getTargetId(),
                    request.getTargetType()
            );

            // NotificationEmailDto로 변환하여 Kafka에 발행
            NotificationEmailDto notificationEmailDto = notification.toDto();
            publishMessageUseCase.publishMessage(mailNotificationTopic, notificationEmailDto);

            log.info("유사동물 메일 알림 발송 완료: userId={}, id={}", request.getUserId(), notification.getId());
        } catch (Exception e) {
            throw new BaseException(EMAIL_SEND_FAIL);
        }

    }

    @Override
    public void processMailNotificationMessage(String jsonString){
        try {
            // 1. JSON 파싱
            NotificationEmailDto notificationEmailDto = notificationUtils.parseJsonToEmailDto(jsonString);
            // 2. 이메일 전송
            customMailSenderUseCase.sendSimilarAdoptionEmail(notificationEmailDto);

        } catch (JsonProcessingException e) {
            log.error("JSON 파싱 실패: jsonString={}, error={}", jsonString, e.getMessage(), e);
            throw new BaseException(EMAIL_INVALID_JSON_FORMAT);
        } catch (Exception e) {
            log.error("이메일 처리 실패", e);
            throw e;
        }

    }

    private void checkDuplicatedEmail(String email) {
        Boolean emails = queryUserDataUseCase.isEmailExist(email);
        if (emails == true) {
            throw new BaseException(EMAIL_DUPLICATE);
        }
    }

    @Override
    public Boolean verifiedCode(String email, String authCode) {
        String redisKey = AUTH_CODE_PREFIX + email;
        String redisAuthCode = redisUtils.getData(redisKey);

        // 1) Redis에서 꺼낸 값과 입력값을 로그로 출력
        log.info("[Email Verification] Redis key = {}, storedCode = {}, inputCode = {}",
                redisKey, redisAuthCode, authCode);

        // 2) 비교 전에 null 체크 추가 (null일 경우 NPE 방지)
        if (redisAuthCode == null) {
            log.warn("[Email Verification] No code found in Redis for key={}", redisKey);
            return false;
        }

        // 3) 실제 비교
        if (!redisAuthCode.equals(authCode)) {
            log.warn("[Email Verification] 코드 불일치: storedCode={}, inputCode={}",
                    redisAuthCode, authCode);
            return false;
        }

        // 4) 코드가 일치하면 삭제 후 true
        redisUtils.deleteData(redisKey);
        log.info("[Email Verification] 코드 일치. Redis에서 key={} 삭제 후 true 반환", redisKey);
        return true;
    }
}

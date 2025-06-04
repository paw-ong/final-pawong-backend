package kr.co.pawong.pwbe.notification.application.service;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.EMAIL_SEND_FAIL;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import kr.co.pawong.pwbe.notification.application.port.in.CustomMailSenderUseCase;
import kr.co.pawong.pwbe.notification.application.port.out.NotificationPort;
import kr.co.pawong.pwbe.notification.application.service.dto.NotificationEmailDto;
import kr.co.pawong.pwbe.notification.domain.Notification;
import kr.co.pawong.pwbe.notification.enums.TargetType;
import kr.co.pawong.pwbe.user.application.port.in.QueryEmailUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomMailSenderService implements CustomMailSenderUseCase {

    private final JavaMailSender javaMailSender;
    private final QueryEmailUseCase queryEmailUseCase;
    private final NotificationPort notificationPort;
    private final SpringTemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String username;

    // 이메일 인증
    public void sendCodeEmail(String toEmail,
            String title,
            String authCode){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(new InternetAddress(username,"포옹","UTF-8")); // 보내는 사람 주소임, smtp 설정과 맞아야함
            helper.setTo(toEmail); // 받는 사람 주소임
            helper.setSubject(title); // 메일 제목 설정

            // html에 들어갈 동적데이터 설정하기
            HashMap<String, String> emailValues = new HashMap<>();
            emailValues.put("authCode", authCode);
            String templateName = "codeMail";
            String text = setContext(emailValues,templateName);

            helper.setText(text, true);

            javaMailSender.send(mimeMessage);                                     // HTML 메일 전송 호출

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new BaseException(EMAIL_SEND_FAIL);
        }

    }

    // 유사 공고 알림
    public void sendSimilarAdoptionEmail(NotificationEmailDto notificationEmailDto){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        Long userId = notificationEmailDto.getUserId();
        Long targetId = notificationEmailDto.getTargetId();
        TargetType targetType = notificationEmailDto.getTargetType();
        String toEmail = queryEmailUseCase.getEmailByUserId(notificationEmailDto.getUserId());
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(new InternetAddress(username,"포옹","UTF-8")); // 보내는 사람 주소임, smtp 설정과 맞아야함
            helper.setTo(toEmail); // 받는 사람 주소임
            helper.setSubject(notificationEmailDto.getTitle()); // 메일 제목 설정

            // html에 들어갈 동적데이터 설정하기
            HashMap<String, String> emailValues = new HashMap<>();
            emailValues.put("targetId", targetId.toString());
            emailValues.put("targetType", targetType.toString());
            String templateName = "similarAdoptionMail";
            String text = setContext(emailValues,templateName);

            helper.setText(text, true);

            javaMailSender.send(mimeMessage);                                     // HTML 메일 전송 호출

            // DB에 저장
            Notification notification = Notification.createSimilarAdoptionMailNotification(
                    userId, targetId, targetType);

            Notification savedEmailNotification = notificationPort.save(notification);
            log.debug("유사 공고 이메일 저장 완료: id={}", savedEmailNotification.getId());

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new BaseException(EMAIL_SEND_FAIL);
        }
    }


    private String setContext(Map<String, String> emailValues, String templateName) {
        Context context = new Context();
        emailValues.forEach(context::setVariable);
        return templateEngine.process(templateName, context);
    }

}

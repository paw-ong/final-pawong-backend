package kr.co.pawong.pwbe.notification.application.service;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.USER_NOT_FOUND;

import java.time.Duration;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import kr.co.pawong.pwbe.global.util.CodeGenerator;
import kr.co.pawong.pwbe.global.util.RedisUtils;
import kr.co.pawong.pwbe.notification.application.port.in.MailUseCase;
import kr.co.pawong.pwbe.user.application.port.in.QueryUserDataUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService implements MailUseCase {

    private static final String AUTH_CODE_PREFIX = "AuthCode ";
    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;
    private final CustomMailSenderService mailService;
    private final RedisUtils redisUtils;
    private final QueryUserDataUseCase queryUserDataUseCase;

    @Override
    public void sendCodeToEmail(String toEmail) {
        this.checkDuplicatedEmail(toEmail);
        String title = "Travel with me 이메일 인증 번호";
        String authCode = CodeGenerator.generateCode();
        mailService.sendEmail(toEmail, title, authCode);
        // 이메일 인증 요청 시 인증 번호 Redis에 저장 ( key = "AuthCode " + Email / value = AuthCode )
        redisUtils.setDataExpire(AUTH_CODE_PREFIX + toEmail,
                authCode, Duration.ofMillis(this.authCodeExpirationMillis));
    }

    private void checkDuplicatedEmail(String email) {
        Boolean emails = queryUserDataUseCase.isEmailExist(email);
        if (emails == true) {
            throw new BaseException(USER_NOT_FOUND);
        }
    }

    @Override
    public String verifiedCode(String email, String authCode) {
        this.checkDuplicatedEmail(email);
        String redisAuthCode = redisUtils.getData(AUTH_CODE_PREFIX + email);
        boolean authResult = redisAuthCode.equals(authCode);
        if (authResult) {
            redisUtils.deleteData(AUTH_CODE_PREFIX + email);
            return "이메일 인증 성공";
        }
        return "이메일 인증 실패";
    }
}

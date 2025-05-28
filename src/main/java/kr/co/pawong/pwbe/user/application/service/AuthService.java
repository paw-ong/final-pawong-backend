package kr.co.pawong.pwbe.user.application.service;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.USER_NOT_FOUND;

import jakarta.transaction.Transactional;
import kr.co.pawong.pwbe.user.application.port.in.AuthUseCase;
import kr.co.pawong.pwbe.user.application.port.in.dto.AuthResponse;
import java.time.Duration;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import kr.co.pawong.pwbe.global.util.CodeGenerator;
import kr.co.pawong.pwbe.global.util.RedisUtils;
import kr.co.pawong.pwbe.user.domain.User;
import kr.co.pawong.pwbe.user.application.port.in.dto.UserCreate;
import kr.co.pawong.pwbe.user.application.port.in.dto.UserUpdate;
import kr.co.pawong.pwbe.user.application.port.out.UserDataCommandPort;
import kr.co.pawong.pwbe.user.application.port.out.UserDataQueryPort;
import kr.co.pawong.pwbe.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService implements AuthUseCase {

    private final UserDataQueryPort userDataQueryPort;
    private final UserDataCommandPort userCommandRepository;

  private static final String AUTH_CODE_PREFIX = "AuthCode ";
  @Value("${spring.mail.auth-code-expiration-millis}")
  private long authCodeExpirationMillis;
  private final EmailService mailService;
  private final RedisUtils redisUtils;

  @Override
  public User createOrGetUser(UserCreate userCreate) {
    User getUser =  userDataQueryPort.findByUserSocialId(userCreate.getSocialId());
    if(getUser == null) {
      return userCommandRepository.save(User.from(userCreate));
    }
    return getUser;
  }

    @Transactional
    @Override
    public AuthResponse signUp(Long userId, UserUpdate userUpdate) {
        User pendingUser = userDataQueryPort.findByUserIdOrThrow(userId);
        User updatedUser = userCommandRepository.updateProfile(pendingUser.update(userUpdate));
        return new AuthResponse(
                updatedUser.getUserId(),
                updatedUser.getStatus()
        );
    }

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
    Boolean emails = userDataQueryPort.findByEmail(email);
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

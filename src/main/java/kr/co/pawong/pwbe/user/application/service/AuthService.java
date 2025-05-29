package kr.co.pawong.pwbe.user.application.service;

import jakarta.transaction.Transactional;
import kr.co.pawong.pwbe.notification.application.service.CustomMailSenderService;
import kr.co.pawong.pwbe.user.application.port.in.AuthUseCase;
import kr.co.pawong.pwbe.user.application.port.in.dto.AuthResponse;
import kr.co.pawong.pwbe.global.security.util.JwtTokenProvider;
import kr.co.pawong.pwbe.user.domain.User;
import java.time.Duration;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import kr.co.pawong.pwbe.global.util.CodeGenerator;
import kr.co.pawong.pwbe.global.util.RedisUtils;
import kr.co.pawong.pwbe.user.domain.User;
import kr.co.pawong.pwbe.user.application.port.in.dto.UserCreate;
import kr.co.pawong.pwbe.user.application.port.in.dto.UserUpdate;
import kr.co.pawong.pwbe.user.application.port.out.UserDataCommandPort;
import kr.co.pawong.pwbe.user.application.port.out.UserDataQueryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

    private final UserDataQueryPort userDataQueryPort;
    private final UserDataCommandPort userCommandRepository;



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
}

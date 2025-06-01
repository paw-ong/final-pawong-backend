package kr.co.pawong.pwbe.user.application.service;

import jakarta.transaction.Transactional;
import kr.co.pawong.pwbe.user.application.port.in.AuthUseCase;
import kr.co.pawong.pwbe.user.application.port.in.dto.AuthResponse;
import kr.co.pawong.pwbe.user.domain.User;
import kr.co.pawong.pwbe.user.application.port.in.dto.UserCreate;
import kr.co.pawong.pwbe.user.application.port.in.dto.UserUpdate;
import kr.co.pawong.pwbe.user.application.port.out.UserDataCommandPort;
import kr.co.pawong.pwbe.user.application.port.out.UserDataQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

    private final UserDataQueryPort userDataQueryPort;
    private final UserDataCommandPort userCommandRepository;

    public User createOrGetUser(UserCreate userCreate) {
        User getUser = userDataQueryPort.findByUserSocialId(userCreate.getSocialId());
        if (getUser == null) {
            return userCommandRepository.save(User.from(userCreate));
        }
        return getUser;
    }

    @Override
    @Transactional
    public AuthResponse signUp(Long userId, UserUpdate userUpdate) {
        User pendingUser = userDataQueryPort.findByUserIdOrThrow(userId);
        User updatedUser = userCommandRepository.updateProfile(pendingUser.update(userUpdate));
        return new AuthResponse(
                updatedUser.getUserId(),
                updatedUser.getStatus()
        );
    }
}

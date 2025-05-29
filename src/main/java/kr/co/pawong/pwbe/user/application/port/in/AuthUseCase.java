package kr.co.pawong.pwbe.user.application.port.in;

import kr.co.pawong.pwbe.user.domain.User;
import kr.co.pawong.pwbe.user.application.port.in.dto.AuthResponse;
import kr.co.pawong.pwbe.user.application.port.in.dto.UserCreate;
import kr.co.pawong.pwbe.user.application.port.in.dto.UserUpdate;

public interface AuthUseCase {

    User createOrGetUser(UserCreate userCreate);

    AuthResponse signUp(Long userId, UserUpdate userUpdate);
}

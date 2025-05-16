package kr.co.pawong.pwbe.user.application.port.out;

import kr.co.pawong.pwbe.user.domain.User;

public interface UserDataCommandPort {

    User save(User user);

    User updateProfile(User user);
}

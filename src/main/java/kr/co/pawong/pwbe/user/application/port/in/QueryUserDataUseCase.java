package kr.co.pawong.pwbe.user.application.port.in;

import kr.co.pawong.pwbe.user.domain.User;

public interface QueryUserDataUseCase {

    User getUser(Long userId);

    User getUserBySocialId(Long socialId);

    Boolean isEmailExist(String email);

}

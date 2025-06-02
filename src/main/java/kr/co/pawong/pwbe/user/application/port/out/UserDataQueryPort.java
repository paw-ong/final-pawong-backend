package kr.co.pawong.pwbe.user.application.port.out;

import kr.co.pawong.pwbe.user.domain.User;

public interface UserDataQueryPort {

    User findByUserIdOrThrow(Long userId);

    User findByUserSocialId(Long socialId);

    Boolean findByEmail(String email);
}

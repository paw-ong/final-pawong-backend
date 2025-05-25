package kr.co.pawong.pwbe.user.adapter.out.jpa;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.USER_NOT_FOUND;

import kr.co.pawong.pwbe.global.error.exception.BaseException;
import kr.co.pawong.pwbe.user.adapter.out.jpa.entity.UserEntity;
import kr.co.pawong.pwbe.user.adapter.out.jpa.repository.UserJpaRepository;
import kr.co.pawong.pwbe.user.application.port.out.UserDataQueryPort;
import kr.co.pawong.pwbe.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserDataQueryAdapter implements UserDataQueryPort {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User findByUserIdOrThrow(Long userId) {
        return userJpaRepository.findByUserId(userId)
                .map(UserEntity::toDomain)
                .orElseThrow(() ->
                        new BaseException(USER_NOT_FOUND)
                );
    }

    @Override
    public User findByUserSocialId(Long socialId) {
        return userJpaRepository.findBySocialId(socialId)
                .map(UserEntity::toDomain)
                .orElse(null);
    }
}

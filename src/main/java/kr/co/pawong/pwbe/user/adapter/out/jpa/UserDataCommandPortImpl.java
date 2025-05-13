package kr.co.pawong.pwbe.user.adapter.out.jpa;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.USER_NOT_FOUND;

import kr.co.pawong.pwbe.global.error.exception.BaseException;
import kr.co.pawong.pwbe.user.adapter.out.jpa.repository.UserJpaRepository;
import kr.co.pawong.pwbe.user.domain.User;
import kr.co.pawong.pwbe.user.application.port.out.UserDataCommandPort;
import kr.co.pawong.pwbe.user.adapter.out.jpa.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserDataCommandPortImpl implements UserDataCommandPort {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        return userJpaRepository.save(UserEntity.of(user))
                .toDomain();
    }

    @Override
    public User updateProfile(User user) {
        return userJpaRepository.findByUserId(user.getUserId())
                .orElseThrow(() ->
                        new BaseException(USER_NOT_FOUND)
                )
                .updateProfile(user)
                .toDomain();
    }
}

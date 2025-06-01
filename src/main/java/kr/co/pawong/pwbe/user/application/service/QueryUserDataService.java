package kr.co.pawong.pwbe.user.application.service;

import kr.co.pawong.pwbe.user.application.port.in.QueryNicknameUseCase;
import kr.co.pawong.pwbe.user.application.port.in.QueryUserDataUseCase;
import kr.co.pawong.pwbe.user.application.port.out.UserDataQueryPort;
import kr.co.pawong.pwbe.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QueryUserDataService implements QueryUserDataUseCase, QueryNicknameUseCase {

    private final UserDataQueryPort userDataQueryPort;

    @Override
    @Transactional(readOnly = true)
    public User getUser(Long userId) {
        return userDataQueryPort.findByUserIdOrThrow(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserBySocialId(Long socialId) {
        return userDataQueryPort.findByUserSocialId(socialId);
    }

    @Override
    @Transactional(readOnly = true)
    public String getNicknameByUserId(Long userId) {
        return userDataQueryPort.findByUserIdOrThrow(userId)
                .getNickname();
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean isEmailExist(String email) {
        return userDataQueryPort.findByEmail(email);
    }
}

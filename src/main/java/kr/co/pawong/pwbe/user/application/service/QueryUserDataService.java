package kr.co.pawong.pwbe.user.application.service;

import kr.co.pawong.pwbe.user.application.port.in.QueryNicknameUseCase;
import kr.co.pawong.pwbe.user.domain.User;
import kr.co.pawong.pwbe.user.application.port.out.UserDataQueryPort;
import kr.co.pawong.pwbe.user.application.port.in.QueryUserDataUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueryUserDataService implements QueryUserDataUseCase, QueryNicknameUseCase {

    private final UserDataQueryPort userDataQueryPort;

    @Override
    public User getUser(Long userId) {
        return userDataQueryPort.findByUserIdOrThrow(userId);
    }

    @Override
    public User getUserBySocialId(Long socialId) {
        return userDataQueryPort.findByUserSocialId(socialId);
    }

    @Override
    public String getNicknameByUserId(Long userId) {
        return userDataQueryPort.findByUserIdOrThrow(userId)
                .getNickname();
    }
}

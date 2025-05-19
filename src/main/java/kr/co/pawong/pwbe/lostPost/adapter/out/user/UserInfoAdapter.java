package kr.co.pawong.pwbe.lostPost.adapter.out.user;

import kr.co.pawong.pwbe.lostPost.application.port.out.UserInfoPort;
import kr.co.pawong.pwbe.user.application.port.in.QueryBookmarkDataUseCase;
import kr.co.pawong.pwbe.user.application.port.in.QueryNicknameUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserInfoAdapter implements UserInfoPort {

    private final QueryNicknameUseCase queryUserDataUseCase;

    @Override
    public String getNicknameByUserId(Long userId) {
        return queryUserDataUseCase.getNicknameByUserId(userId);
    }
}

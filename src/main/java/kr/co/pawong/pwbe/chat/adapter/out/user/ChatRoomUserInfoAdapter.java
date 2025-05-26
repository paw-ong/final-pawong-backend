package kr.co.pawong.pwbe.chat.adapter.out.user;

import kr.co.pawong.pwbe.chat.application.port.out.ChatRoomUserInfoPort;
import kr.co.pawong.pwbe.user.adapter.out.dto.ChatRoomUserInfo;
import kr.co.pawong.pwbe.user.application.port.in.QueryNicknameUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// 특정 유저의 닉네임을 조회하는 adapter
@Component
@RequiredArgsConstructor
public class ChatRoomUserInfoAdapter implements ChatRoomUserInfoPort {

    private final QueryNicknameUseCase queryNicknameUseCase;

    @Override
    public ChatRoomUserInfo getUserNameById(Long userId) {
        String nicknameByUserId = queryNicknameUseCase.getNicknameByUserId(userId);
        return new ChatRoomUserInfo(nicknameByUserId);
    }

}

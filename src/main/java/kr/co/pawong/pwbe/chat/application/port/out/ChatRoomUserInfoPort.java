package kr.co.pawong.pwbe.chat.application.port.out;

import kr.co.pawong.pwbe.user.adapter.out.dto.ChatRoomUserInfo;

public interface ChatRoomUserInfoPort {

    ChatRoomUserInfo getUserNameById(Long userId);

}

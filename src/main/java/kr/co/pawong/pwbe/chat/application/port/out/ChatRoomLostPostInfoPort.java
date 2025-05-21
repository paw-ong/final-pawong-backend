package kr.co.pawong.pwbe.chat.application.port.out;

import kr.co.pawong.pwbe.lostPost.application.port.in.dto.ChatRoomLostPostInfo;

public interface ChatRoomLostPostInfoPort {

    ChatRoomLostPostInfo getLostPostInfosById(Long postId);
}

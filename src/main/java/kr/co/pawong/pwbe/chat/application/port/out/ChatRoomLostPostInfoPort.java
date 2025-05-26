package kr.co.pawong.pwbe.chat.application.port.out;

import kr.co.pawong.pwbe.chat.adapter.out.lostPost.dto.ChatRoomLostPostInfo;

public interface ChatRoomLostPostInfoPort {

    ChatRoomLostPostInfo getLostPostInfosById(Long postId);
}

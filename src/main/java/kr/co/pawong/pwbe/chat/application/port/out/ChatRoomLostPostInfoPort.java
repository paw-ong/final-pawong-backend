package kr.co.pawong.pwbe.chat.application.port.out;

import kr.co.pawong.pwbe.chat.adapter.out.lostPost.dto.ChatRoomLostPostInfo;

public interface ChatRoomLostPostInfoPort {

    // ChatRoom에서 활용할 ChatRoomLostPost 정보들
    ChatRoomLostPostInfo getLostPostInfosById(Long postId);
}

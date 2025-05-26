package kr.co.pawong.pwbe.chat.application.port.out;

import kr.co.pawong.pwbe.chat.adapter.out.lostPost.dto.ChatRoomLostPostAuthorInfo;
import kr.co.pawong.pwbe.chat.adapter.out.lostPost.dto.ChatRoomLostPostInfo;

public interface ChatRoomLostPostInfoPort {

    ChatRoomLostPostAuthorInfo getLostPostAuthorInfoById(Long postId);

    ChatRoomLostPostInfo getLostPostInfosById(Long postId);
}

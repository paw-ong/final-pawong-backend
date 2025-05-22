package kr.co.pawong.pwbe.lostPost.application.port.in;

import java.util.List;
import kr.co.pawong.pwbe.chat.adapter.out.lostPost.dto.ChatRoomLostPostInfo;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostDetailResponse;

public interface QueryLostPostDataUseCase {

    List<LostPostCard> getLostPostsByUserId(Long userId);

    LostPostDetailResponse findLostPostById(Long lostPostId);

    ChatRoomLostPostInfo findChatRoomLostPostInfosById(Long lostPostId);
}

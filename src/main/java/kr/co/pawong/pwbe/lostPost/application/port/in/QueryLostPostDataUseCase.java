package kr.co.pawong.pwbe.lostPost.application.port.in;

import java.util.List;
import kr.co.pawong.pwbe.chat.adapter.out.lostPost.dto.ChatRoomLostPostInfo;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostDetailDto;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.SliceLostPostSearchResponses;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import org.springframework.data.domain.Pageable;

public interface QueryLostPostDataUseCase {

    List<LostPostCard> getLostPostsByUserId(Long userId);

    LostPostDetailDto findLostPostById(Long lostPostId);

    ChatRoomLostPostInfo findChatRoomLostPostInfosById(Long lostPostId);

    SliceLostPostSearchResponses fetchSlicedLostPosts(Pageable pageable, PostType type, Long userId);

    List<Long> getUserIdsByLostPostIds(List<Long> lostPostIds);
}

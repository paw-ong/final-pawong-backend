package kr.co.pawong.pwbe.lostPost.application.port.in;

import java.util.List;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import kr.co.pawong.pwbe.lostPost.enums.PostType;

public interface QueryLostPostDataUseCase {

    List<LostPostCard> getLostPostsByUserId(Long userId);

    List<LostPostCard> getLostPostsByPostType(PostType postType);

}

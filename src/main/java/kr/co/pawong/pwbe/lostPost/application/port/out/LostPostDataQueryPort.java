package kr.co.pawong.pwbe.lostPost.application.port.out;

import java.util.List;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;

public interface LostPostDataQueryPort {

    List<LostPost> getLostPostsByUserId(Long userId);

    LostPost findLostPostByIdOrThrow(Long lostPostId);

}

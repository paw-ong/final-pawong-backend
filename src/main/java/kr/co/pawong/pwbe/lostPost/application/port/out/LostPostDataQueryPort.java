package kr.co.pawong.pwbe.lostPost.application.port.out;

import java.util.List;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LostPostDataQueryPort {

    List<LostPost> getLostPostsByUserId(Long userId);

    LostPost findLostPostByIdOrThrow(Long lostPostId);

    Page<LostPost> getLostPostsByPostTypePaged(Pageable pageable, PostType type);

}

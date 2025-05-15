package kr.co.pawong.pwbe.lostPost.application.port.in;

import java.util.List;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostDetailResponse;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.SliceLostPostSearchResponses;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import org.springframework.data.domain.Pageable;

public interface QueryLostPostDataUseCase {

    List<LostPostCard> getLostPostsByUserId(Long userId);

    LostPostDetailResponse findLostPostById(Long lostPostId);

    SliceLostPostSearchResponses fetchSlicedLostPosts(Pageable pageable, PostType type);
}

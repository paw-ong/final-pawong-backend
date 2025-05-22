package kr.co.pawong.pwbe.lostPost.application.port.in;

import kr.co.pawong.pwbe.lostPost.adapter.in.api.dto.request.LostPostSearchRequest;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostSearchResponses;
import org.springframework.data.domain.Pageable;

public interface SearchLostPostDataUseCase {

    LostPostSearchResponses searchLostPosts(LostPostSearchRequest request, Pageable pageable);

}

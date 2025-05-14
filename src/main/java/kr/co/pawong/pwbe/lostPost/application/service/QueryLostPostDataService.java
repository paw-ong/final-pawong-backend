package kr.co.pawong.pwbe.lostPost.application.service;

import kr.co.pawong.pwbe.lostPost.application.port.in.QueryLostPostDataUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostDetailDto;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostDetailResponse;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostPostDataQueryPort;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueryLostPostDataService implements QueryLostPostDataUseCase {

    private final LostPostDataQueryPort lostPostDataQueryPort;

    public LostPostDetailResponse findLostPostById(Long lostPostId) {
        LostPost lostPost = lostPostDataQueryPort.findLostPostById(lostPostId);
        LostPostDetailDto lostPostDetailDto = LostPostDetailDto.from(lostPost);
        return new LostPostDetailResponse(lostPostDetailDto);
    }

}

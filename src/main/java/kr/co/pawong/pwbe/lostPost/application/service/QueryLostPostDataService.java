package kr.co.pawong.pwbe.lostPost.application.service;

import java.time.Clock;
import java.util.List;
import kr.co.pawong.pwbe.lostPost.application.port.in.QueryLostPostDataUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAnimalQuery;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostDetailDto;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostDetailResponse;
import kr.co.pawong.pwbe.lostPost.application.port.in.mapper.LostPostCardMapper;
import kr.co.pawong.pwbe.lostPost.application.port.in.mapper.LostPostDetailMapper;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostPostDataQueryPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.UserInfoPort;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QueryLostPostDataService implements QueryLostPostDataUseCase {

    private final LostPostDataQueryPort lostPostDataQueryPort;
    private final UserInfoPort userInfoPort;
    private final Clock clock;

    @Override
    @Transactional(readOnly = true)
    public List<LostPostCard> getLostPostsByUserId(Long userId) {

        List<LostPost> lostPosts = lostPostDataQueryPort.getLostPostsByUserId(userId);
        String author = userInfoPort.getNicknameByUserId(userId);

        return lostPosts.stream()
                .map(post -> LostPostCardMapper.toLostPostCard(post, author, clock)).toList();
    }

    @Override
    public LostPostDetailResponse findLostPostById(Long lostPostId) {

        LostPost lostPost = lostPostDataQueryPort.findLostPostByIdOrThrow(lostPostId);
        String author = userInfoPort.getNicknameByUserId(lostPost.getUserId());

        LostPostDetailDto lostPostDetailDto = LostPostDetailMapper.toModel(lostPost, author, clock);

        return new LostPostDetailResponse(lostPostDetailDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LostPostCard> getLostAnimalsByPostIds(List<LostAnimalQuery> lostAnimalQueries) {
        return null;
    }
}

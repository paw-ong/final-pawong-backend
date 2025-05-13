package kr.co.pawong.pwbe.lostPost.application.service;

import java.util.List;
import kr.co.pawong.pwbe.global.time.TimeUtils;
import kr.co.pawong.pwbe.lostPost.application.port.in.QueryLostPostDataUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import kr.co.pawong.pwbe.lostPost.application.port.in.mapper.LostPostCardMapper;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostPostDataQueryPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.UserInfoPort;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueryLostPostDataService implements QueryLostPostDataUseCase {

    private final LostPostDataQueryPort lostPostDataQueryPort;
    private final UserInfoPort userInfoPort;
    private final TimeUtils timeUtils;

    @Override
    public List<LostPostCard> getLostPostsByUserId(Long userId) {

        List<LostPost> lostPosts = lostPostDataQueryPort.getLostPostsByUserId(userId);
        String author = userInfoPort.getNicknameByUserId(userId);

        return lostPosts.stream()
                .map(post -> LostPostCardMapper.toLostPostCard(timeUtils, post, author))
                .toList();
    }
}

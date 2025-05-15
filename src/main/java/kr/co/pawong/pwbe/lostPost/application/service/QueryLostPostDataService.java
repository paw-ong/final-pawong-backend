package kr.co.pawong.pwbe.lostPost.application.service;

import java.time.Clock;
import java.util.List;
import kr.co.pawong.pwbe.lostPost.application.port.in.QueryLostPostDataUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import kr.co.pawong.pwbe.lostPost.application.port.in.mapper.LostPostCardMapper;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostPostDataQueryPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.UserInfoPort;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
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
                .map(post -> LostPostCardMapper.toLostPostCard(post, author, clock))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LostPostCard> getLostPostsByPostType(PostType postType){

        List<LostPost> lostPosts = lostPostDataQueryPort.getLostPostsByPostType(postType);

        return lostPosts.stream()
                .map(post -> {
                    String author = userInfoPort.getNicknameByUserId(post.getUserId());
                    return LostPostCardMapper.toLostPostCard(post, author, clock);
                })
                .toList();
    }

}

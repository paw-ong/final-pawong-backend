package kr.co.pawong.pwbe.user.application.service;

import java.util.List;
import kr.co.pawong.pwbe.user.application.port.in.QueryBookmarkDataUseCase;
import kr.co.pawong.pwbe.user.application.port.in.QueryMyPageDataUseCase;
import kr.co.pawong.pwbe.user.application.port.in.dto.MyPageLostPostResponse;
import kr.co.pawong.pwbe.user.application.port.out.LostPostInfoPort;
import kr.co.pawong.pwbe.user.application.port.in.mapper.MyPageMapper;
import kr.co.pawong.pwbe.user.application.port.out.dto.MyPageLostPostInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QueryMyPageDataService implements QueryMyPageDataUseCase {

    private final LostPostInfoPort lostPostInfoPort;

    private final QueryBookmarkDataUseCase queryBookmarkDataUseCase;

    @Override
    @Transactional(readOnly = true)
    public List<MyPageLostPostResponse> getLostPostsByUserId(Long userId) {
        List<MyPageLostPostInfo> myPageLostPosts = lostPostInfoPort.getLostPostsByUserId(userId);

        return myPageLostPosts.stream()
                .map(MyPageMapper::toMyPageLostPostResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MyPageLostPostResponse> getBookmarkedLostPostsByUserId(Long userId) {
        List<MyPageLostPostInfo> myPageLostPosts = lostPostInfoPort.getLostAnimalsByLostPostIds(
                queryBookmarkDataUseCase.getBookmarksByUserId(userId)
        );

        return myPageLostPosts.stream()
                .map(MyPageMapper::toMyPageLostPostResponse)
                .toList();
    }

}

package kr.co.pawong.pwbe.user.application.service;

import java.util.ArrayList;
import java.util.List;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionCard;
import kr.co.pawong.pwbe.user.application.port.in.QueryBookmarkDataUseCase;
import kr.co.pawong.pwbe.user.application.port.in.QueryFavoritesDataUseCase;
import kr.co.pawong.pwbe.user.application.port.in.QueryMyPageDataUseCase;
import kr.co.pawong.pwbe.user.application.port.in.dto.MyPageFavoritesResponse;
import kr.co.pawong.pwbe.user.application.port.in.dto.MyPageLostPostResponse;
import kr.co.pawong.pwbe.user.application.port.in.mapper.MyPageMapper;
import kr.co.pawong.pwbe.user.application.port.out.LostPostInfoPort;
import kr.co.pawong.pwbe.user.application.port.out.dto.MyPageLostPostInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QueryMyPageDataService implements QueryMyPageDataUseCase {

    private final LostPostInfoPort lostPostInfoPort;

    private final QueryBookmarkDataUseCase queryBookmarkDataUseCase;

    private final QueryFavoritesDataUseCase queryFavoritesDataUseCase;

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
        List<MyPageLostPostInfo> myPageLostPosts = lostPostInfoPort.getLostAnimalsByIds(
                queryBookmarkDataUseCase.getBookmarksByUserId(userId)
        );

        return myPageLostPosts.stream()
                .map(MyPageMapper::toMyPageLostPostResponse)
                .toList();
    }

    // 유저의 찜 목록을 AdoptionCard 리스트로 제공
    @Override
    @Transactional(readOnly = true)
    public List<MyPageFavoritesResponse> getFavoritesListByUserId(Long userId) {
        List<AdoptionCard> adoptionCards = queryFavoritesDataUseCase.getAllFavoritesAdoptionCardsByUserId(
                userId);

        List<MyPageFavoritesResponse> myPageFavoritesListResponses = new ArrayList<>(
                adoptionCards.size());
        for (AdoptionCard adoptionCard : adoptionCards) {
            myPageFavoritesListResponses.add(new MyPageFavoritesResponse(adoptionCard));
        }
        return myPageFavoritesListResponses;
    }

}

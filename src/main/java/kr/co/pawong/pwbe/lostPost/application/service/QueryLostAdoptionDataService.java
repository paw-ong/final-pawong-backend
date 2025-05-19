package kr.co.pawong.pwbe.lostPost.application.service;

import java.time.Clock;
import java.util.List;
import java.util.stream.Collectors;
import kr.co.pawong.pwbe.lostPost.application.port.in.QueryLostAdoptionDataUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAdoptionDetailDto;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAdoptionDetailResponse;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.SliceLostPostSearchResponses;
import kr.co.pawong.pwbe.lostPost.application.port.in.mapper.LostAdoptionDetailMapper;
import kr.co.pawong.pwbe.lostPost.application.port.in.mapper.LostPostCardMapper;
import kr.co.pawong.pwbe.lostPost.application.port.out.BookmarkInfoPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostAdoptionDataQueryPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.ShelterCareNmPort;
import kr.co.pawong.pwbe.lostPost.domain.LostAdoption;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueryLostAdoptionDataService implements QueryLostAdoptionDataUseCase {

    private final LostAdoptionDataQueryPort lostAdoptionDataQueryPort;
    private final ShelterCareNmPort shelterCareNmPort;
    private final BookmarkInfoPort bookmarkInfoPort;
    private final Clock clock;

    @Override
    public LostAdoptionDetailResponse findAdoptionById(Long adoptionId) {

        LostAdoption lostAdoption = lostAdoptionDataQueryPort.findAdoptionById(adoptionId);
        String careNm = shelterCareNmPort.getShelterCareNmByCareRegNo(lostAdoption.getCareRegNo());

        LostAdoptionDetailDto lostAdoptionDetailDto
                = LostAdoptionDetailMapper.toModel(lostAdoption, careNm);
        return new LostAdoptionDetailResponse(lostAdoptionDetailDto);
    }

    @Override
    public SliceLostPostSearchResponses fetchSlicedLostAdoptions(Pageable pageable, Long userId) {
        Page<LostAdoption> lostAdoptionPage = lostAdoptionDataQueryPort.getLostAdoptionsPaged(pageable);
        List<LostPostCard> lostPostCards = mapToLostPostCards(lostAdoptionPage, clock, userId);
        boolean hasNext = lostAdoptionPage.hasNext();
        return new SliceLostPostSearchResponses(hasNext,lostPostCards);
    }

    private List<LostPostCard> mapToLostPostCards(Page<LostAdoption> lostAdoptionPage, Clock clock, Long userId) {
        return lostAdoptionPage.getContent().stream()
                .map(la -> {
                    String shelter = shelterCareNmPort.getShelterCareNmByCareRegNo(la.getCareRegNo());
                    boolean bookmarked = bookmarkInfoPort.existsByUserIdAndAdoptionId(userId, la.getAdoptionId());
                    return LostPostCardMapper.toLostPostCard(la, shelter, bookmarked,clock);
                })
                .collect(Collectors.toList());
    }

}

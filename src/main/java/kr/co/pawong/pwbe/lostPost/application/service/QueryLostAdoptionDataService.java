package kr.co.pawong.pwbe.lostPost.application.service;

import java.time.Clock;
import java.util.List;
import java.util.stream.Collectors;
import kr.co.pawong.pwbe.adoption.application.port.out.ProxyUrlPort;
import kr.co.pawong.pwbe.adoption.application.port.out.ShelterInfoPort;
import kr.co.pawong.pwbe.lostPost.adapter.in.api.dto.response.LostAdoptionDetailResponse;
import kr.co.pawong.pwbe.lostPost.application.port.in.QueryLostAdoptionDataUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAdoptionDetailDto;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.SliceLostPostSearchResponses;
import kr.co.pawong.pwbe.lostPost.application.port.in.mapper.LostAdoptionDetailMapper;
import kr.co.pawong.pwbe.lostPost.application.port.in.mapper.LostPostCardMapper;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostAdoptionDataQueryPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.ShelterCareNmPort;
import kr.co.pawong.pwbe.lostPost.domain.LostAdoption;
import kr.co.pawong.pwbe.shelter.application.port.in.dto.ShelterDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueryLostAdoptionDataService implements QueryLostAdoptionDataUseCase {

    private final LostAdoptionDataQueryPort lostAdoptionDataQueryPort;
    private final ShelterCareNmPort shelterCareNmPort;
    private final Clock clock;
    private final ProxyUrlPort proxyUrlPort;
    private final ShelterInfoPort shelterInfoPort;

    @Override
    public LostAdoptionDetailResponse getLostAdoptionDetails(Long adoptionId){
        LostAdoption lostAdoption = lostAdoptionDataQueryPort.findAdoptionByIdOrThrow(adoptionId);
        changePopfilesToProxy(lostAdoption);
        String careNm = shelterCareNmPort.getShelterCareNmByCareRegNo(lostAdoption.getCareRegNo());

        LostAdoptionDetailDto lostAdoptionDetailDto = LostAdoptionDetailMapper.toModel(lostAdoption, careNm);
        ShelterDetailDto shelterDetailDto = shelterInfoPort.getShelterDetail(lostAdoption.getCareRegNo());

        return new LostAdoptionDetailResponse(lostAdoptionDetailDto, shelterDetailDto);
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
                    changePopfilesToProxy(la);
                    String shelter = shelterCareNmPort.getShelterCareNmByCareRegNo(la.getCareRegNo());
                    return LostPostCardMapper.toLostPostCard(la, shelter, clock);
                })
                .collect(Collectors.toList());
    }

    private void changePopfilesToProxy(LostAdoption lostAdoption) {
        String popfile1 = proxyUrlPort.generateProxyUrl(lostAdoption.getPopfile1());
        String popfile2 = proxyUrlPort.generateProxyUrl(lostAdoption.getPopfile2());
        lostAdoption.updatePopfile1(popfile1);
        lostAdoption.updatePopfile2(popfile2);
    }
}

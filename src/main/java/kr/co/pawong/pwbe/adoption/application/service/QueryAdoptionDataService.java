package kr.co.pawong.pwbe.adoption.application.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import kr.co.pawong.pwbe.adoption.domain.model.Adoption;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionCard;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionDetailDto;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.SliceAdoptionSearchResponses;
import kr.co.pawong.pwbe.adoption.application.port.out.AdoptionDataQueryPort;
import kr.co.pawong.pwbe.adoption.application.port.out.ShelterInfoPort;
import kr.co.pawong.pwbe.adoption.application.service.support.AdoptionCardMapper;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionRecommendResponses;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionDetailResponse;
import kr.co.pawong.pwbe.adoption.application.port.in.QueryAdoptionDataUseCase;
import kr.co.pawong.pwbe.shelter.application.port.in.dto.ShelterInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class  QueryAdoptionDataService implements QueryAdoptionDataUseCase {

    private final AdoptionDataQueryPort adoptionDataQueryPort;

    /**
     * DB에 저장된 모든 Adoption 도메인 객체를 조회하여 반환합니다.
     *
     * @return 전체 Adoption 리스트
     */
    @Override
    public List<Adoption> getAllAdoptions() {
        return adoptionDataQueryPort.findAll();
    }

    private final ShelterInfoPort shelterInfoPort;

    // infinite scroll을 위한 slice 방식
    @Override
    public SliceAdoptionSearchResponses fetchSlicedAdoptions(Pageable pageable) {
        Page<Adoption> adoptionPage = adoptionDataQueryPort.findAllPaged(pageable);
        List<AdoptionCard> adoptionCards = mapToAdoptionCards(adoptionPage);
        boolean hasNext = adoptionPage.hasNext();
        return new SliceAdoptionSearchResponses(hasNext, adoptionCards);
    }

    private List<AdoptionCard> mapToAdoptionCards(Page<Adoption> adoptionPage) {
        return adoptionPage.getContent().stream()
                .map(AdoptionCardMapper::toAdoptionCard)
                .collect(Collectors.toList());
    }

    @Override
    public AdoptionRecommendResponses getRecommendAdoptions() {
        LocalDate today = LocalDate.now();
        List<Adoption> adoptions = adoptionDataQueryPort.findTop12ActiveByNoticeEdt(today);
        // 각 입양 정보의 noticeEdt와 activeState 로그 출력
//        for (Adoption adoption : adoptions) {
//            log.info("AdoptionId: {}, noticeEdt: {}, activeState: {}",
//                    adoption.getAdoptionId(),
//                    adoption.getNoticeEdt(),
//                    adoption.getActiveState());
//        }

        List<AdoptionCard> adoptionCards = adoptions.stream()
                .map(AdoptionCardMapper::toAdoptionCard)
                .toList();

        return new AdoptionRecommendResponses(adoptionCards);
    }

    @Override
    public ShelterInfoDto findShelterInfoByAdoptionId(Long adoptionId) {
        // 1) AdoptionEntity에서 careRegNo 조회
        String careRegNo = adoptionDataQueryPort.findCareRegNoByAdoptionId(adoptionId);
        if (careRegNo == null) {
            log.warn("careRegNo not found for adoptionId: {}", adoptionId);
            return null; // 또는 예외 throw
        }
        // 2) ShelterAdapter 통해 실제 Shelter 컨텍스트에 질의
        return shelterInfoPort.getShelterInfo(careRegNo);
    }


    @Override
    public AdoptionDetailResponse getAdoptionDetail(Long adoptionId) {
        // 1) Adoption 엔티티 조회
        Adoption adoption = adoptionDataQueryPort.findByAdoptionIdOrThrow(adoptionId);

        // 2) AdoptionDetailDto로 매핑
        AdoptionDetailDto adoptionDetailDto = AdoptionDetailDto.from(adoption);

        // 3) Port를 통해 ShelterDetail 조회
        var shelterDetailDto = shelterInfoPort.getShelterDetail(adoptionDetailDto.getCareRegNo());

        // 4) Response 생성 후 반환
        return new AdoptionDetailResponse(adoptionDetailDto, shelterDetailDto);
    }
}
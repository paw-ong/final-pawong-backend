package kr.co.pawong.pwbe.adoption.application.service;

import kr.co.pawong.pwbe.adoption.application.port.out.AdoptionAiPort;
import kr.co.pawong.pwbe.adoption.domain.model.Adoption;
import kr.co.pawong.pwbe.adoption.application.port.out.dto.AdoptionSearchCondition;
import kr.co.pawong.pwbe.adoption.application.port.out.AdoptionDataQueryPort;
import kr.co.pawong.pwbe.adoption.application.port.out.AdoptionEngineQueryPort;
import kr.co.pawong.pwbe.adoption.application.service.support.AdoptionCardMapper;
import kr.co.pawong.pwbe.adoption.application.service.support.AdoptionSearchMapper;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionCard;
import kr.co.pawong.pwbe.adoption.adapter.in.api.dto.request.AdoptionSearchRequest;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionIdSearchResponse;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionIdSearchResponses;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionSearchResponses;
import kr.co.pawong.pwbe.adoption.application.port.in.SearchAdoptionEngineUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchAdoptionEngineService implements SearchAdoptionEngineUseCase {
    private final AdoptionEngineQueryPort adoptionEngineQueryPort;
    private final AdoptionDataQueryPort adoptionDataQueryPort;
    private final AdoptionAiPort adoptionAiPort;

    // RDB에서 adoptionId로 최종 AdoptionSearchResponses 반환
    @Override
    public AdoptionSearchResponses search(AdoptionSearchRequest request) {
        // request에서 ES의 adoptionId 갖고오기
        AdoptionIdSearchResponses adoptionIdSearchResponses = searchDocumentIds(request);
        // adoptionId를 활용해 RDB 조회한 결과 Adoption 객체 반환
        List<Adoption> adoptions = adoptionIdSearchResponses.getAdoptionResponseList().stream()
                .map(AdoptionIdSearchResponse::getAdoptionId)
                .map(adoptionDataQueryPort::findByIdOrThrow)
                .collect(Collectors.toList());
        // 최종적으로 검색 결과를 위한 매핑 리스트 반환
        List<AdoptionCard> adoptionCards = adoptions.stream()
                .map(AdoptionCardMapper::toAdoptionCard)
                .collect(Collectors.toList());

        return new AdoptionSearchResponses(adoptionCards);
    }

    // ES에서 검색 시 adoptionId를 반환
    @Override
    public AdoptionIdSearchResponses searchDocumentIds(AdoptionSearchRequest request) {
//        String refinedSearchTerm = refineSearchTerm(request);
        String refinedSearchTerm = request.getSearchTerm();
        List<String> tags = tag(request);
        AdoptionSearchCondition condition = AdoptionSearchMapper.fromRequest(request, refinedSearchTerm, tags, embed(refinedSearchTerm));

        List<Adoption> adoptions = adoptionEngineQueryPort.searchSimilarAdoptions(condition);
        return new AdoptionIdSearchResponses(adoptions.stream()
                .map(AdoptionSearchMapper::toIdSearchResponse)
                .collect(Collectors.toList()));
    }

    // 위임, 정제된 검색어 문장
    private String refineSearchTerm(AdoptionSearchRequest request) {
        String term = request.getSearchTerm();
        return adoptionAiPort.refineSpecialMark(term);
    }

    // 위임, 태깅
    private List<String> tag(AdoptionSearchRequest request) {
        return adoptionAiPort.tag(request.getSearchTerm());
    }

    // 위임, 임베딩 값
    private float[] embed(String refinedSearchTerm) {
        return adoptionAiPort.embed(refinedSearchTerm);
    }

    @Override
    public List<String> autocomplete(String keyword) {
        return adoptionEngineQueryPort.autocomplete(keyword);
    }

}

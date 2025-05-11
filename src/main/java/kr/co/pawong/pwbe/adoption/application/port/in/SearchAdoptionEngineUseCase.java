package kr.co.pawong.pwbe.adoption.application.port.in;

import kr.co.pawong.pwbe.adoption.adapter.in.api.dto.request.AdoptionSearchRequest;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionSearchResponses;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionIdSearchResponses;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SearchAdoptionEngineUseCase {

    // RDB에서 adoptionId로 최종 adoptions 반환
    AdoptionSearchResponses search(AdoptionSearchRequest request);

    // ES에서 조회해서 adoptionIds 반환
    AdoptionIdSearchResponses searchDocumentIds(AdoptionSearchRequest request);

    // ES에서 조회해서 자동완성 리스트 반환
    List<String> autocomplete(String keyword);
}

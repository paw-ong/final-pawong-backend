package kr.co.pawong.pwbe.adoption.application.port.out;

import kr.co.pawong.pwbe.adoption.domain.model.Adoption;
import kr.co.pawong.pwbe.adoption.application.port.out.dto.AdoptionSearchCondition;
import kr.co.pawong.pwbe.adoption.adapter.out.persistence.es.document.AdoptionDocument;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;

public interface AdoptionEngineQueryPort {
    // Adoption 리스트로 반환
    List<Adoption> searchSimilarAdoptions(AdoptionSearchCondition condition);

    // 검색 결과의 전체 metadata 포함
    SearchHits<AdoptionDocument> searchSimilarAdoptionSearchHits(AdoptionSearchCondition condition);

    // 자동 완성
    List<String> autocomplete(String keyword);
}

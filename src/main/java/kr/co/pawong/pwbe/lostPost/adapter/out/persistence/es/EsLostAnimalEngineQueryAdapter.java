package kr.co.pawong.pwbe.lostPost.adapter.out.persistence.es;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Objects;
import kr.co.pawong.pwbe.lostPost.adapter.out.persistence.es.document.LostAnimalDocument;
import kr.co.pawong.pwbe.lostPost.adapter.out.persistence.es.query.LostAnimalQueryBuilder;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostAnimalEngineQueryPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.dto.LostAnimalEngineRequest;
import kr.co.pawong.pwbe.lostPost.application.port.out.dto.LostAnimalEngineResponse;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EsLostAnimalEngineQueryAdapter implements LostAnimalEngineQueryPort {

    private final ElasticsearchOperations elasticsearchOperations;
    private final LostAnimalQueryBuilder queryBuilder;

    @Override
    public List<LostAnimalEngineResponse> searchSimilarLostAnimals(LostAnimalEngineRequest request) {
        // 1) 쿼리 생성
        Query esQuery = queryBuilder.build(request);   // script_score 포함

        // 2) 검색 실행 (minScore 0.9)
        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(esQuery)
                .withMinScore(0.9f) // cosineSimilarity 컷
                .build();

        SearchHits<LostAnimalDocument> hits =
                elasticsearchOperations.search(searchQuery, LostAnimalDocument.class);

        // 3) 문서를 응답 DTO로 매핑
        return hits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .filter(Objects::nonNull)
                .map(doc -> new LostAnimalEngineResponse(
                        doc.getRdbId(),
                        PostType.valueOf(doc.getType())))
                .toList();
    }
}

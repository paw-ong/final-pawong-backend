package kr.co.pawong.pwbe.adoption.adapter.out.persistence.adapter;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import kr.co.pawong.pwbe.adoption.domain.model.Adoption;
import kr.co.pawong.pwbe.adoption.application.port.out.dto.AdoptionSearchCondition;
import kr.co.pawong.pwbe.adoption.application.port.out.AdoptionEngineQueryPort;
import kr.co.pawong.pwbe.adoption.adapter.out.persistence.query.AdoptionQueryBuilder;
import kr.co.pawong.pwbe.adoption.adapter.out.persistence.document.AdoptionDocument;
import kr.co.pawong.pwbe.adoption.adapter.out.persistence.document.AutocompleteDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Repository
@RequiredArgsConstructor
public class EsAdoptionEngineQueryAdapter implements AdoptionEngineQueryPort {

    private final ElasticsearchOperations elasticsearchOperations;
    private final AdoptionQueryBuilder queryBuilder;

    @Override
    public List<Adoption> searchSimilarAdoptions(AdoptionSearchCondition condition) {
        return searchSimilarAdoptionSearchHits(condition).stream()
            .map(SearchHit::getContent)
            .map(AdoptionDocument::toModel)
            .toList();
    }

    @Override
    public SearchHits<AdoptionDocument> searchSimilarAdoptionSearchHits(
        AdoptionSearchCondition condition) {
        Query finalQuery = queryBuilder.buildFinalQuery(condition);

        NativeQuery searchQuery = NativeQuery.builder()
            .withQuery(finalQuery)
            .withMaxResults(20)
            .build();

        return elasticsearchOperations.search(searchQuery, AdoptionDocument.class);
    }

    @Override
    public List<String> autocomplete(String keyword) {
        var edgeQuery = NativeQuery.builder()
            .withQuery(q -> q.match(m -> m.field("word.edge").query(keyword)))
            .withPageable(PageRequest.of(0, 1000))
            .build();
        var ngramQuery = NativeQuery.builder()
            .withQuery(q -> q.bool(b -> b
                .must(m1 -> m1.match(m2 -> m2.field("word.ngram").query(keyword)))
                .mustNot(mn -> mn.match(mn2 -> mn2.field("word.edge").query(keyword)))
            ))
            .withPageable(PageRequest.of(0, 1000))
            .build();

        return Stream.concat(
                elasticsearchOperations.search(edgeQuery, AutocompleteDocument.class)
                    .stream().map(hit -> hit.getContent().getWord()),
                elasticsearchOperations.search(ngramQuery, AutocompleteDocument.class)
                    .stream().map(hit -> hit.getContent().getWord())
            )
            .toList();
    }
}

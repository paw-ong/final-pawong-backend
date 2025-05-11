package kr.co.pawong.pwbe.adoption.adapter.out.persistence.query;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kr.co.pawong.pwbe.adoption.application.port.out.dto.AdoptionSearchCondition;
import kr.co.pawong.pwbe.adoption.application.port.out.dto.AdoptionSearchCondition.Region;
import kr.co.pawong.pwbe.infrastructure.ai.AdoptionAiService;
import org.springframework.stereotype.Component;

@Component
public class AdoptionQueryBuilder {

    public Query buildFinalQuery(AdoptionSearchCondition condition) {
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();
        boolQuery.must(buildFilterQuery(condition));
        boolQuery.must(buildSemanticQuery(condition));
        return boolQuery.build()._toQuery();
    }

    public Query buildFilterQuery(AdoptionSearchCondition condition) {
        BoolQuery.Builder filter = new BoolQuery.Builder();

        if (condition.getUpKindCds() != null && !condition.getUpKindCds().isEmpty()) {
            filter.filter(f -> f.terms(t -> t
                    .field("upKindCd")
                    .terms(ts -> ts.value(condition.getUpKindCds().stream()
                            .map(cd -> FieldValue.of(cd.toString()))
                            .toList()))
            ));
        }

        if (condition.getSexCd() != null) {
            filter.filter(f -> f.term(t -> t
                    .field("sexCd")
                    .value(condition.getSexCd().toString())
            ));
        }

        if (condition.getNeuterYn() != null) {
            filter.filter(f -> f.term(t -> t
                    .field("neuterYn")
                    .value(condition.getNeuterYn().toString())
            ));
        }

        if (condition.getRegions() != null && !condition.getRegions().isEmpty()) {

            BoolQuery.Builder regionBool = new BoolQuery.Builder();

            for (Region region : condition.getRegions()) {
                BoolQuery.Builder inner = new BoolQuery.Builder()
                        .filter(f -> f.term(t -> t
                                .field("city")
                                .value(FieldValue.of(region.getCity()))
                        ));

                if (region.getDistrict() != null) {
                    inner.filter(f -> f.term(t -> t
                            .field("district")
                            .value(FieldValue.of(region.getDistrict()))
                    ));
                }
                regionBool.should(q -> q.bool(inner.build()));
            }

            filter.filter(f -> f.bool(regionBool.build()));
        }

        return filter.build()._toQuery();
    }

    public Query buildSemanticQuery(AdoptionSearchCondition condition) {
        BoolQuery.Builder semantic = new BoolQuery.Builder();

        if (condition.getRefinedSearchTerm() != null && !condition.getRefinedSearchTerm()
                .isEmpty()) {
            semantic.should(s -> s.match(m -> m
                    .field("refinedSpecialMark")
                    .query(condition.getRefinedSearchTerm())
                    .analyzer("korean")
                    .boost(1.0f)    // 형태소는 1배의 가중치
            ));
        }

        // tags가 있으면 tagsField에 terms 매칭 추가
        if (condition.getTags() != null && !condition.getTags().isEmpty()) {
            for (String tag : condition.getTags()) {
                semantic.should(s -> s.term(t -> t
                        .field("tagsField")
                        .value(tag)
                        .boost(4.0f)      // 각 태그 매칭 시 점수 가중치
                ));
            }
        }

        if (condition.getEmbedding() != null
                && condition.getEmbedding().length == AdoptionAiService.EMBEDDING_DIMENSION) {
            List<Float> embeddingList = new ArrayList<>(condition.getEmbedding().length);
            for (float f : condition.getEmbedding()) {
                embeddingList.add(f);
            }

            semantic.filter(f -> f.exists(e -> e.field("embedding")))
                    .should(s -> s.scriptScore(ss -> ss
                            .query(q -> q.matchAll(m -> m))
                            .script(sc -> sc.inline(i -> i
                                    .source("cosineSimilarity(params.query_vector, 'embedding') + 1.0") // cosine similarity 활용
                                    .params("query_vector", JsonData.of(embeddingList))
                            ))
                            .boost(4.0f)    // 벡터는 2배의 가중치
                    ));
        }

        return semantic.build()._toQuery();
    }
}

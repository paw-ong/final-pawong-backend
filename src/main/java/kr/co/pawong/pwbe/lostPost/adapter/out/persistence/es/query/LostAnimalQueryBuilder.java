package kr.co.pawong.pwbe.lostPost.adapter.out.persistence.es.query;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;
import java.util.ArrayList;
import java.util.List;
import kr.co.pawong.pwbe.lostPost.application.port.out.dto.LostAnimalEngineRequest;
import org.springframework.stereotype.Component;

@Component
public class LostAnimalQueryBuilder {

    /** lost_animal 쿼리 */
    public Query build(LostAnimalEngineRequest req) {

        /* ---------- type 필터 ---------- */
        BoolQuery.Builder bool = new BoolQuery.Builder();
        if (req.types() != null && !req.types().isEmpty()) {
            bool.filter(f -> f.terms(t -> t
                    .field("type")
                    .terms(v -> v.value(
                            req.types().stream()
                                    .map(p -> FieldValue.of(p.name()))
                                    .toList()))));
        }

        /* ---------- embedding 유사도 ---------- */
        if (req.embedding() != null && req.embedding().length == 512) {
            List<Float> vec = new ArrayList<>(req.embedding().length);
            for (float v : req.embedding()) vec.add(v);

            return Query.of(q -> q.scriptScore(ss -> ss
                    .query(bool.build()._toQuery())
                    .script(s -> s.inline(i -> i
                            .source("cosineSimilarity(params.query_vector, 'embedding')") // -1~1
                            .params("query_vector", JsonData.of(vec))))));
        }

        /* 벡터가 없으면 필터만 */
        return Query.of(q -> q.bool(bool.build()));
    }
}

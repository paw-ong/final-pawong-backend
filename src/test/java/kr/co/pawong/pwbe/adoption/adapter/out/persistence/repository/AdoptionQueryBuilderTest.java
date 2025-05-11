package kr.co.pawong.pwbe.adoption.adapter.out.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import java.io.IOException;
import java.util.List;
import kr.co.pawong.pwbe.adoption.application.port.out.dto.AdoptionSearchCondition;
import kr.co.pawong.pwbe.adoption.application.port.out.dto.AdoptionSearchCondition.Region;
import kr.co.pawong.pwbe.adoption.adapter.out.persistence.query.AdoptionQueryBuilder;
import kr.co.pawong.pwbe.adoption.adapter.out.persistence.document.TestDocument;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchClientAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.elasticsearch.DataElasticsearchTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.testcontainers.junit.jupiter.Testcontainers;

@Disabled("ES 통합 테스트는 전체 빌드 시 제외")
@Testcontainers
// ──────────────────────────────────────────────────────────
// @Container 로 정의한 ElasticsearchContainer를 테스트 시작 전후로
// 자동으로 띄우고 종료해 줍니다.
// ──────────────────────────────────────────────────────────
@DataElasticsearchTest(
    includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = AdoptionQueryBuilder.class
    ),
    excludeAutoConfiguration = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
    }
)
// ────────────────────────────────────────────────────────────
// Spring Data Elasticsearch 전용 슬라이스 테스트를 실행합니다.
// - includeFilters: AdoptionQueryBuilder 같은 필요한 빈만 포함
// - excludeAutoConfiguration: JPA/DataSource 관련 자동설정 비활성화
// ────────────────────────────────────────────────────────────
@ImportAutoConfiguration({
    ElasticsearchClientAutoConfiguration.class,
    ElasticsearchDataAutoConfiguration.class
})
// ─────────────────────────────────────────────────────────────────────
// Spring Boot의 Elasticsearch Java-client(ELC) 자동설정을 명시적으로 import.
// ─────────────────────────────────────────────────────────────────────
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdoptionQueryBuilderTest extends TestElasticsearchContainer {

  @Autowired ElasticsearchOperations operations;
  @Autowired AdoptionQueryBuilder queryBuilder;
  @Autowired ElasticsearchClient client;

  @BeforeAll
  void 인덱스_초기화_및_색인() throws IOException {
    if (operations.indexOps(TestDocument.class).exists())
      operations.indexOps(TestDocument.class).delete();
    String INDEX = "test";
    List<TestDocument> testDocuments = List.of(
        new TestDocument(1L,"인천광역시","부평구"),
        new TestDocument(2L,"서울특별시","광진구"),
        new TestDocument(3L,"서울특별시","노원구"),
        new TestDocument(4L,"경기도","안양시"),
        new TestDocument(5L,"경기도","안성시")
    );
    BulkRequest.Builder bulk = new BulkRequest.Builder();
    testDocuments.forEach(doc ->
        bulk.operations(op -> op
            .index(idx -> idx
                .index(INDEX)
                .id(doc.getId().toString())
                .document(doc)
            )
        )
    );
    client.bulk(bulk.build());
    client.indices().refresh(r -> r.index(INDEX));
  }

  @Test
  void 지역검색어가_여러개라면_합집합으로_조회() {
    var condition = AdoptionSearchCondition.builder()
        .regions(List.of(
            new Region("인천광역시", "부평구"),
            new Region("서울특별시", "광진구")
        ))
        .build();

    var hits = operations.search(
        NativeQuery.builder()
            .withQuery(queryBuilder.buildFilterQuery(condition))
            .build(),
        TestDocument.class);
    var ids = hits.stream()
        .map(h->h.getContent().getId())
        .toList();

    assertThat(hits.getTotalHits()).isEqualTo(2);       // 두 개 문서가 조회되어야 함
    assertThat(ids).containsExactlyInAnyOrder(1L, 2L);
  }

  @Test
  void 도시_전체_검색어일_경우() {
    var condition = AdoptionSearchCondition.builder()
        .regions(List.of(
            new Region("경기도", null)
        ))
        .build();

    var hits = operations.search(
        NativeQuery.builder()
            .withQuery(queryBuilder.buildFilterQuery(condition))
            .build(),
        TestDocument.class);
    var ids = hits.stream()
        .map(h->h.getContent().getId())
        .toList();

    assertThat(hits.getTotalHits()).isEqualTo(2);       // 두 개 문서가 조회되어야 함
    assertThat(ids).containsExactlyInAnyOrder(4L, 5L);
  }

  @Test
  void 도시_전체_검색어와_일반_지역_검색어일_경우() {
    var condition = AdoptionSearchCondition.builder()
        .regions(List.of(
            new Region("경기도", null),
            new Region("서울특별시", "노원구")
        ))
        .build();

    var hits = operations.search(
        NativeQuery.builder()
            .withQuery(queryBuilder.buildFilterQuery(condition))
            .build(),
        TestDocument.class);
    var ids = hits.stream()
        .map(h->h.getContent().getId())
        .toList();

    assertThat(hits.getTotalHits()).isEqualTo(3);       // 3개 문서 조회
    assertThat(ids).containsExactlyInAnyOrder(3L, 4L, 5L);
  }

}
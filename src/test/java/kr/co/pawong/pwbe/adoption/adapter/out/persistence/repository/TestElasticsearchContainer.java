package kr.co.pawong.pwbe.adoption.adapter.out.persistence.repository;

import org.junit.jupiter.api.Disabled;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * 통합 테스트용 Elasticsearch 컨테이너 구성
 * Docker를 띄운 상태에서 실행해야 동작합니다.
 */
@Disabled("ES 통합 테스트는 전체 빌드 시 제외")
@Testcontainers
public abstract class TestElasticsearchContainer {
  @Container
  static final ElasticsearchContainer CONTAINER =
      new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:8.13.0")
          .withEnv("discovery.type", "single-node")
          .withEnv("xpack.security.enabled", "false")
          .withReuse(true);
  static {
    CONTAINER.start();
  }
  @DynamicPropertySource
  static void registerElasticProperties(DynamicPropertyRegistry registry) {
    // spring.elasticsearch.uris[0] 에 컨테이너 호스트:포트를 바인딩
    registry.add("spring.elasticsearch.uris[0]", CONTAINER::getHttpHostAddress);
  }
}

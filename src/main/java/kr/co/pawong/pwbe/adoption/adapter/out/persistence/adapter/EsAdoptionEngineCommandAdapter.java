package kr.co.pawong.pwbe.adoption.adapter.out.persistence.adapter;

import java.util.List;
import java.util.Objects;
import kr.co.pawong.pwbe.adoption.application.port.out.dto.AdoptionEsDto;
import kr.co.pawong.pwbe.adoption.application.port.out.AdoptionEngineCommandPort;
import kr.co.pawong.pwbe.adoption.enums.ActiveState;
import kr.co.pawong.pwbe.adoption.adapter.out.persistence.document.AdoptionDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class EsAdoptionEngineCommandAdapter implements AdoptionEngineCommandPort {
    private final ElasticsearchOperations elasticsearchOperations;

    // 저장될 인덱스 정의
    private static final IndexCoordinates INDEX = IndexCoordinates.of("adoption");

    /**
     * 여러 Adoption 객체를 500개씩 Elasticsearch에 벌크로 저장
     */
    @Override
    public void saveAdoptionToEs(List<AdoptionEsDto> adoptionEsDtos) {
        try {
            // 빈 리스트인 경우 처리하지 않고 반환
            if (adoptionEsDtos.isEmpty()) {
                return;
            }

            int batchSize = 500; // 한 번에 저장할 크기
            int totalSaved = 0; // ES에 저장된 데이터 총 개수

            for (int i = 0; i < adoptionEsDtos.size(); i += batchSize) {
                int end = Math.min(i + batchSize, adoptionEsDtos.size());
                List<AdoptionEsDto> batch = adoptionEsDtos.subList(i, end);

                // Active 상태의 Adoption만 AdoptionDocument로 반환
                List<IndexQuery> queries = batch.stream()
                        .filter(adoptionEsDto -> ActiveState.ACTIVE == adoptionEsDto.getActiveState())
                        .map(adoptionEsDto -> {
                            try {
                                // Adoption -> AdoptionDocument
                                AdoptionDocument adoptionDocument = AdoptionDocument.from(adoptionEsDto);
                                // ES에 저장할 IndexQuery 생성
                                return new IndexQueryBuilder()
                                        .withIndex(INDEX.getIndexName())
                                        .withObject(adoptionDocument)
                                        .build();
                            } catch (Exception e) {
                                log.error("동물 ID: {}의 문서 변환 중 오류 발생: {}", adoptionEsDto.getAdoptionId(),
                                        e.getMessage());
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .toList();

                log.info("변환된 쿼리 수: {}", queries.size());

                // 저장할 데이터가 없으면 다음 배치로 넘어감
                if (queries.isEmpty()) {
                    log.warn("저장할 ACTIVE 상태의 동물 데이터가 없습니다.");
                    continue;
                }

                // ES에 벌크 저장
                elasticsearchOperations.bulkIndex(queries, INDEX);
                log.info("{}개의 ACTIVE 상태 동물 데이터가 Elasticsearch에 성공적으로 저장되었습니다.", queries.size());

                totalSaved += queries.size();
            }
            // 전체 저장된 ACTIVE 데이터 개수 로그
            log.info("총 {}개의 ACTIVE 상태 동물 데이터가 Elasticsearch에 저장되었습니다.", totalSaved);

        } catch(Exception e){
            log.error("Elasticsearch 벌크 저장 중 오류 발생: {}", e.getMessage(), e);
        }
    }
}


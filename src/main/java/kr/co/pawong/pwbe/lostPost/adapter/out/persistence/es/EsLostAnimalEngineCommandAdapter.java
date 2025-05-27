package kr.co.pawong.pwbe.lostPost.adapter.out.persistence.es;

import kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import kr.co.pawong.pwbe.lostPost.adapter.out.persistence.es.document.LostAnimalDocument;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostAnimalEngineCommandPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.dto.LostAnimalDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class EsLostAnimalEngineCommandAdapter implements LostAnimalEngineCommandPort {

    private final ElasticsearchOperations elasticsearchOperations;

    private static final IndexCoordinates INDEX = IndexCoordinates.of("lost_animal");

    @Override
    public void saveLostAnimalToEs(LostAnimalDto lostAnimalDto) {
        try {
            if (lostAnimalDto == null){
                return;
            }

            LostAnimalDocument document = LostAnimalDocument.from(lostAnimalDto);

            elasticsearchOperations.save(document, INDEX);
            log.info("LostAnimal ES 저장이 완료되었습니다: {}", lostAnimalDto.getLostAnimalId());
        } catch (Exception e) {
            log.error("LostAnimal ES 저장이 실패하였습니다.", e);
            throw new BaseException(CustomErrorCode.ES_SAVE_ERROR);
        }
    }
}

package kr.co.pawong.pwbe.adoption.adapter.out.persistence.adapter;

import jakarta.transaction.Transactional;
import java.util.List;
import kr.co.pawong.pwbe.adoption.domain.model.Adoption;
import kr.co.pawong.pwbe.adoption.application.port.out.AdoptionDataCommandPort;
import kr.co.pawong.pwbe.adoption.adapter.out.persistence.repository.AdoptionJpaRepository;
import kr.co.pawong.pwbe.adoption.adapter.out.persistence.entity.AdoptionEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JpaAdoptionDataCommandAdapter implements AdoptionDataCommandPort {

    private final AdoptionJpaRepository adoptionJpaRepository;

    /**
     * Adoption 도메인 리스트를 AdoptionEntity로 변환하여 DB에 저장합니다.
     *
     * @param adoptions 저장할 Adoption 리스트
     */
    @Override
    public void saveAdoptions(List<Adoption> adoptions) {
        List<AdoptionEntity> adoptionEntities = adoptions.stream()
                .map(AdoptionEntity::from)
                .toList();

        adoptionJpaRepository.saveAll(adoptionEntities);
        log.info("{}개의 입양 정보가 저장되었습니다.", adoptions.size());
    }

    /**
     * 전달받은 Adoption 리스트 각각의 embeddingDone 값을 adoptionId 기준으로 부분 업데이트합니다.
     *
     * @param adoptions embeddingDone 값을 반영할 Adoption 리스트
     */
    @Override
    @Transactional
    public void updateIsEmbedded(List<Adoption> adoptions) {
        for (Adoption adoption : adoptions) {
            adoptionJpaRepository.updateIsEmbedded(adoption.getAdoptionId(), adoption.isEmbedded());
        }
    }

    /**
     * 전달받은 Adoption 리스트의 refinedSpecialMark, tagsField, aiProcessed 값을
     * adoptionId 기준으로 DB에 부분 업데이트합니다.
     * (10개씩 등 batch로 호출 가능)
     *
     * @param adoptions 업데이트할 Adoption 리스트
     */
    @Override
    @Transactional
    public void updateAiFields(List<Adoption> adoptions) {
        for (Adoption adoption : adoptions) {
            log.info("updateAiFields: adoptionId={}, searchField={}, tagsField={}, aiProcessed={}",
                    adoption.getAdoptionId(), adoption.getRefinedSpecialMark(), adoption.getTagsField(), adoption.isAiProcessed());
            adoptionJpaRepository.updateAiFields(
                    adoption.getAdoptionId(),
                    adoption.getRefinedSpecialMark(),
                    adoption.getTagsField(),
                    adoption.isAiProcessed()
            );
        }
    }
}


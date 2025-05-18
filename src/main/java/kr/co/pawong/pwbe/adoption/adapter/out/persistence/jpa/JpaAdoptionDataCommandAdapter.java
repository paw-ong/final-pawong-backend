package kr.co.pawong.pwbe.adoption.adapter.out.persistence.jpa;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.ADOPTION_NOT_FOUND;

import jakarta.transaction.Transactional;
import java.util.List;
import kr.co.pawong.pwbe.adoption.adapter.out.persistence.jpa.entity.AdoptionEntity;
import kr.co.pawong.pwbe.adoption.adapter.out.persistence.jpa.repository.AdoptionJpaRepository;
import kr.co.pawong.pwbe.adoption.application.port.out.AdoptionDataCommandPort;
import kr.co.pawong.pwbe.adoption.domain.model.Adoption;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JpaAdoptionDataCommandAdapter implements AdoptionDataCommandPort {

    private final AdoptionJpaRepository adoptionJpaRepository;

    // adoptionId를 기준으로 임베딩 여부 DB에 업데이트
    @Override
    @Transactional
    public void updateIsEmbeddedByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        adoptionJpaRepository.updateIsEmbeddedByIds(ids);
    }

    // 전달받은 Adoption 리스트의 refinedSpecialMark, tagsField, aiProcessed 값을 adoptionId 기준으로 DB에 업데이트
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

    @Override
    @Transactional
    public void updateAdoption(Adoption adoption) {
        // desertionNo로 entity 조회
        AdoptionEntity adoptionEntity = adoptionJpaRepository.findByDesertionNo(
                adoption.getDesertionNo())
                .orElseThrow(() -> new BaseException(ADOPTION_NOT_FOUND));

        // 업데이트 (더티 체킹)
        adoptionEntity.update(adoption);
    }

    @Override
    @Transactional
    public void saveAdoption(Adoption adoption) {
        AdoptionEntity adoptionEntity = AdoptionEntity.from(adoption);
        adoptionJpaRepository.save(adoptionEntity);
    }

    @Override
    @Transactional
    public void deleteAdoption(Adoption adoption) {
        adoptionJpaRepository.deleteById(adoption.getAdoptionId());
    }
}


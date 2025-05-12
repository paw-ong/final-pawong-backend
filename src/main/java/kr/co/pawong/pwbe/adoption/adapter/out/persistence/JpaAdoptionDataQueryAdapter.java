package kr.co.pawong.pwbe.adoption.adapter.out.persistence;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.ADOPTION_NOT_FOUND;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import kr.co.pawong.pwbe.adoption.domain.model.Adoption;
import kr.co.pawong.pwbe.adoption.application.port.out.AdoptionDataQueryPort;
import kr.co.pawong.pwbe.adoption.enums.ActiveState;
import kr.co.pawong.pwbe.adoption.adapter.out.persistence.repository.AdoptionJpaRepository;
import kr.co.pawong.pwbe.adoption.adapter.out.persistence.entity.AdoptionEntity;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaAdoptionDataQueryAdapter implements AdoptionDataQueryPort {

    private final AdoptionJpaRepository adoptionJpaRepository;

    /**
     * DB에 저장된 모든 AdoptionEntity를 Adoption 도메인 객체로 변환하여 반환
     */
    @Override
    public List<Adoption> findAll() {
        return adoptionJpaRepository.findAll().stream()
                .map(AdoptionEntity::toModel)
                .toList();
    }

    @Override
    public String findCareRegNoByAdoptionId(Long adoptionId) {
        return adoptionJpaRepository.findCareRegNoByAdoptionId(adoptionId);
    }

    @Override
    public Adoption findByIdOrThrow(Long id) {
        AdoptionEntity entity = adoptionJpaRepository.findById(id)
                .orElseThrow(() ->
                        new BaseException(ADOPTION_NOT_FOUND));

        return entity.toModel();
    }

    @Override
    public Page<Adoption> findAllPaged(Pageable pageable) {
        Page<AdoptionEntity> entityPage = adoptionJpaRepository.findAll(pageable);
        return entityPage.map(AdoptionEntity::toModel);
    }

    @Override
    public List<Adoption> findTop12ActiveByNoticeEdt(LocalDate today) {
        return adoptionJpaRepository.findTop12ByActiveStateAndNoticeEdtGreaterThanEqualOrderByNoticeEdtAsc(
                        ActiveState.ACTIVE, today)
                .stream()
                .map(AdoptionEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Adoption findByAdoptionIdOrThrow(Long adoptionId) {
        return adoptionJpaRepository.findByAdoptionId(adoptionId)
                .map(AdoptionEntity::toModel)
                .orElseThrow(() ->
                        new BaseException(ADOPTION_NOT_FOUND));
    }

}
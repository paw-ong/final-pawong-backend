package kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.LOST_NOT_FOUND;

import kr.co.pawong.pwbe.global.error.exception.BaseException;
import kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.entity.LostAdoptionEntity;
import kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.entity.LostPostEntity;
import kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.repository.LostAdoptionJpaRepository;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostAdoptionDataQueryPort;
import kr.co.pawong.pwbe.lostPost.domain.LostAdoption;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaLostAdoptionDataQueryAdapter implements LostAdoptionDataQueryPort {

    private final LostAdoptionJpaRepository lostAdoptionJpaRepository;

    public LostAdoption findAdoptionByIdOrThrow(Long adoptionId) {
        LostAdoptionEntity entity = lostAdoptionJpaRepository.findById(adoptionId)
                .orElseThrow(() ->
                        new BaseException(LOST_NOT_FOUND));

        return entity.toDomain();
    }

    @Override
    public Page<LostAdoption> getLostAdoptionsPaged(Pageable pageable) {
        Page<LostAdoptionEntity> entityPage = lostAdoptionJpaRepository.findAll(pageable);
        return entityPage.map(LostAdoptionEntity::toDomain);
    }

}

package kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.LOST_NOT_FOUND;

import kr.co.pawong.pwbe.global.error.exception.BaseException;
import kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.entity.LostAdoptionEntity;
import kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa.repository.LostAdoptionJpaRepository;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostAdoptionDataQueryPort;
import kr.co.pawong.pwbe.lostPost.domain.LostAdoption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaLostAdoptionDataQueryAdapter implements LostAdoptionDataQueryPort {

    private final LostAdoptionJpaRepository lostAdoptionJpaRepository;

    public LostAdoption findAdoptionById(Long adoptionId) {
        LostAdoptionEntity entity = lostAdoptionJpaRepository.findById(adoptionId)
                .orElseThrow(() ->
                        new BaseException(LOST_NOT_FOUND));

        return entity.toDomain();
    }

}

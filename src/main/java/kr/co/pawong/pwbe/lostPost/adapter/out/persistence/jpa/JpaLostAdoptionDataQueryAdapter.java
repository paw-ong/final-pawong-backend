package kr.co.pawong.pwbe.lostPost.adapter.out.persistence.jpa;

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
        return lostAdoptionJpaRepository.findById(adoptionId)
                .get().toDomain();
    }

}

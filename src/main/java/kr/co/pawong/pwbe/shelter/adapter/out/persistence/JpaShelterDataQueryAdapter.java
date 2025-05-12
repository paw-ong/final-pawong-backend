package kr.co.pawong.pwbe.shelter.adapter.out.persistence;

import kr.co.pawong.pwbe.shelter.adapter.out.persistence.repository.ShelterJpaRepository;
import kr.co.pawong.pwbe.shelter.domain.Shelter;
import kr.co.pawong.pwbe.shelter.application.port.out.ShelterDataQueryPort;
import kr.co.pawong.pwbe.shelter.adapter.out.persistence.entity.ShelterEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JpaShelterDataQueryAdapter implements ShelterDataQueryPort {

    private final ShelterJpaRepository shelterJpaRepository;

    @Override
    public Shelter findByCareRegNoOrThrow(String careRegNo) {
        return shelterJpaRepository.findByCareRegNo(careRegNo)
                .map(ShelterEntity::toModel)
                .orElse(null);
    }
}

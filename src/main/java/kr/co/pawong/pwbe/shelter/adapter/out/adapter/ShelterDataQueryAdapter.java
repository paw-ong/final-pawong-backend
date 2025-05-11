package kr.co.pawong.pwbe.shelter.adapter.out.adapter;

import kr.co.pawong.pwbe.shelter.adapter.out.repository.ShelterJpaRepository;
import kr.co.pawong.pwbe.shelter.domain.Shelter;
import kr.co.pawong.pwbe.shelter.application.port.out.ShelterDataQueryPort;
import kr.co.pawong.pwbe.shelter.adapter.out.entity.ShelterEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ShelterDataQueryAdapter implements ShelterDataQueryPort {

    private final ShelterJpaRepository shelterJpaRepository;

    @Override
    public Shelter findByCareRegNoOrThrow(String careRegNo) {
        return shelterJpaRepository.findByCareRegNo(careRegNo)
                .map(ShelterEntity::toModel)
                .orElse(null);
    }
}

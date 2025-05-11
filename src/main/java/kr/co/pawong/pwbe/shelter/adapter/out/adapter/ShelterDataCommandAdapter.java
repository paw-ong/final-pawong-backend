package kr.co.pawong.pwbe.shelter.adapter.out.adapter;

import kr.co.pawong.pwbe.shelter.adapter.out.repository.ShelterJpaRepository;
import kr.co.pawong.pwbe.shelter.domain.Shelter;
import kr.co.pawong.pwbe.shelter.application.port.out.ShelterDataCommandPort;
import kr.co.pawong.pwbe.shelter.adapter.out.entity.ShelterEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ShelterDataCommandAdapter implements ShelterDataCommandPort {

    private final ShelterJpaRepository shelterJpaRepository;

    // Shelter -> ShelterEntity 데이터 저장
    @Override
    public void saveShelters(List<Shelter> shelters) {
        List<ShelterEntity> shelterEntities = shelters.stream()
                .map(ShelterEntity::from)
                .toList();
        shelterJpaRepository.saveAll(shelterEntities);
        log.info("{}개의 보호소 정보가 저장되었습니다.", shelters.size());
    }
    @Override
    public List<String> findAllCareRegNos() {
        return shelterJpaRepository.findAllCareRegNos();
    }

}

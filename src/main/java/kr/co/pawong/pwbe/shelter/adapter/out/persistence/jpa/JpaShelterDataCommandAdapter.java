package kr.co.pawong.pwbe.shelter.adapter.out.persistence.jpa;

import kr.co.pawong.pwbe.shelter.adapter.out.persistence.jpa.repository.ShelterJpaRepository;
import kr.co.pawong.pwbe.shelter.domain.Shelter;
import kr.co.pawong.pwbe.shelter.application.port.out.ShelterDataCommandPort;
import kr.co.pawong.pwbe.shelter.adapter.out.persistence.jpa.entity.ShelterEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JpaShelterDataCommandAdapter implements ShelterDataCommandPort {

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

    @Override
    public void saveShelter(Shelter shelter) {

        ShelterEntity shelterEntity = ShelterEntity.from(shelter);
        shelterJpaRepository.save(shelterEntity);
        log.info("보호소 정보 저장 완료: id={}, careRegNo={}, name={}",
                shelterEntity.getShelterId(), shelterEntity.getCareRegNo(),
                shelterEntity.getCareNm());
    }
}

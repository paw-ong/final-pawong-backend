package kr.co.pawong.pwbe.shelter.application.service;

import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionCareDto;
import kr.co.pawong.pwbe.shelter.application.port.out.ShelterDataQueryPort;
import kr.co.pawong.pwbe.shelter.application.service.support.ShelterMapper;
import kr.co.pawong.pwbe.shelter.domain.Shelter;
import kr.co.pawong.pwbe.shelter.application.port.in.dto.ShelterCreate;
import kr.co.pawong.pwbe.shelter.application.port.out.ShelterDataCommandPort;
import kr.co.pawong.pwbe.shelter.application.port.in.UpdateShelterDataUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateShelterDataService implements UpdateShelterDataUseCase {

    private final ShelterDataQueryPort shelterDataQueryPort;
    private final ShelterDataCommandPort shelterDataCommandPort;
    private final ShelterMapper shelterMapper;

    // ShelterCreate -> Shelter -> Repo에 전달
    @Transactional
    @Override
    public void saveShelters(List<ShelterCreate> shelterCreates) {
        List<Shelter> shelters = shelterCreates.stream()
                .map(Shelter::from)
                .toList();

        shelterDataCommandPort.saveShelters(shelters);
    }

    @Override
    public void updateShelterIfNotExist(AdoptionCareDto adoptionCareDto) {
        String careRegNo = adoptionCareDto.getCareRegNo();
        try {
            // 보호소 번호로 기존 보호소 검색
            boolean exists = shelterDataQueryPort.existsByCareRegNo(careRegNo);

            // 보호소가 존재하지 않는 경우에만 새로 저장
            if (!exists) {
                // 매퍼를 사용하여 DTO를 도메인 객체로 변환
                Shelter shelter = shelterMapper.fromAdoption(adoptionCareDto);
                shelterDataCommandPort.saveShelter(shelter);
            }
        } catch (Exception e) {
            log.error("보호소 정보 업데이트 중 오류 발생: careRegNo={}, error={}",
                    careRegNo, e.getMessage(), e);
            throw e;
        }
    }
}

package kr.co.pawong.pwbe.shelter.adapter.in.api;

import kr.co.pawong.pwbe.shelter.application.port.in.dto.ShelterCreate;
import kr.co.pawong.pwbe.shelter.application.service.ApiShelterService;
import kr.co.pawong.pwbe.shelter.application.port.in.UpdateShelterDataUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/shelters")
@RequiredArgsConstructor
public class ShelterUpdateController {

    private final ApiShelterService apiShelterService;
    private final UpdateShelterDataUseCase updateShelterDataUseCase;

    @PostMapping("/save")
    public ResponseEntity<Void> saveShelters() {
        List<ShelterCreate> shelterCreates = apiShelterService.saveShelters();
        updateShelterDataUseCase.saveShelters(shelterCreates);

        log.info("총 {}개의 동물보호센터가 성공적으로 저장되었습니다.", shelterCreates.size());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}

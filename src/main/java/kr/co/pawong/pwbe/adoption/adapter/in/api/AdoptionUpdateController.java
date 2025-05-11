package kr.co.pawong.pwbe.adoption.adapter.in.api;

import java.util.List;
import kr.co.pawong.pwbe.adoption.domain.model.Adoption;
import kr.co.pawong.pwbe.adoption.application.port.in.UpdateAdoptionEngineUseCase;
import kr.co.pawong.pwbe.adoption.application.port.in.QueryAdoptionDataUseCase;
import kr.co.pawong.pwbe.adoption.application.port.in.UpdateAdoptionDataUseCase;
import kr.co.pawong.pwbe.adoption.application.port.in.ApiRequestUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/adoptions")
@RequiredArgsConstructor
public class AdoptionUpdateController {

    private final ApiRequestUseCase apiRequestUseCase;
    private final QueryAdoptionDataUseCase queryAdoptionDataUseCase;
    private final UpdateAdoptionDataUseCase updateAdoptionDataUseCase;
    private final UpdateAdoptionEngineUseCase updateAdoptionEngineUseCase;

    @PostMapping("/save")
    public ResponseEntity<Void> saveAdoptions() {
        apiRequestUseCase.fetchAndSaveAdoptions();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/save-es")
    public ResponseEntity<Void> saveAdoptionsEs() {
        List<Adoption> adoptions = queryAdoptionDataUseCase.getAllAdoptions();
        updateAdoptionEngineUseCase.saveAdoptionToEs(adoptions);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/ai-preprocessing")
    public ResponseEntity<Void> aiProcessAdoptions() {
        updateAdoptionDataUseCase.aiProcessAdoptions();
        return ResponseEntity.ok().build();
    }

}

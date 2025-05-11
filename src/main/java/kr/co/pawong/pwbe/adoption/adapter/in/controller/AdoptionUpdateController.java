package kr.co.pawong.pwbe.adoption.adapter.in.controller;

import java.util.List;
import kr.co.pawong.pwbe.adoption.domain.model.Adoption;
import kr.co.pawong.pwbe.adoption.application.port.in.UpdateEngineUseCase;
import kr.co.pawong.pwbe.adoption.application.port.in.RetrieveDataUseCase;
import kr.co.pawong.pwbe.adoption.application.port.in.UpdateDataUseCase;
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
    private final RetrieveDataUseCase retrieveDataUseCase;
    private final UpdateDataUseCase updateDataUseCase;
    private final UpdateEngineUseCase updateEngineUseCase;

    @PostMapping("/save")
    public ResponseEntity<Void> saveAdoptions() {
        apiRequestUseCase.fetchAndSaveAdoptions();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/save-es")
    public ResponseEntity<Void> saveAdoptionsEs() {
        List<Adoption> adoptions = retrieveDataUseCase.getAllAdoptions();
        updateEngineUseCase.saveAdoptionToEs(adoptions);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/ai-preprocessing")
    public ResponseEntity<Void> aiProcessAdoptions() {
        updateDataUseCase.aiProcessAdoptions();
        return ResponseEntity.ok().build();
    }

}

package kr.co.pawong.pwbe.adoption.application.port.out;

import java.time.LocalDate;
import java.util.List;
import kr.co.pawong.pwbe.adoption.domain.model.Adoption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdoptionDataQueryPort {
    // AdoptionEntity -> Adoption
    List<Adoption> findAll();

    Adoption findByIdOrThrow(Long adoptionId);

    Page<Adoption> findAllPaged(Pageable pageable);

    String findCareRegNoByAdoptionId(Long adoptionId);

    Adoption findByAdoptionIdOrThrow(Long adoptionId);

    List<Adoption> findTop12ActiveByNoticeEdt(LocalDate today);

    List<Adoption> findAllByDesertionNo(String desertionNo);

    List<Adoption> findByActiveStateInAndAiProcessedFalse();
}
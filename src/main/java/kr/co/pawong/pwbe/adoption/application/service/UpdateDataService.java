package kr.co.pawong.pwbe.adoption.application.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import kr.co.pawong.pwbe.adoption.application.port.out.AdoptionAiPort;
import kr.co.pawong.pwbe.adoption.domain.model.Adoption;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionCreate;
import kr.co.pawong.pwbe.adoption.application.port.out.AdoptionDataQueryPort;
import kr.co.pawong.pwbe.adoption.application.port.out.AdoptionDataCommandPort;
import kr.co.pawong.pwbe.adoption.enums.ActiveState;
import kr.co.pawong.pwbe.adoption.application.port.in.UpdateDataUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateDataService implements UpdateDataUseCase {

    private final AdoptionDataCommandPort adoptionDataCommandPort;
    private final AdoptionDataQueryPort adoptionDataQueryPort;
    private final AdoptionAiPort adoptionAiPort;

    @Value("${adoption.batch-size:50}")
    private int batchSize;

    // AdoptionCreate -> Adoption -> Repo에 전달
    @Override
    public void saveAdoptions(List<AdoptionCreate> adoptionCreates) {
        List<Adoption> adoptions = adoptionCreates.stream()
                .map(Adoption::from)
                .toList();

        adoptionDataCommandPort.saveAdoptions(adoptions);

        log.info("{}개의 입양 정보가 변환 및 저장되었습니다.", adoptions.size());
    }

    /**
     * DB에서 Adoption 도메인 객체를 모두 조회하여 ACTIVE 상태만 AI 전처리(정제) 후 refinedSpecialMark, tagsField,
     * aiProcessed가 변경된 경우만 50개씩 정제하고 50개씩 업데이트
     */
    @Override
    public void aiProcessAdoptions() {
        List<Adoption> adoptions = adoptionDataQueryPort.findAll();

        List<Adoption> activeNotProcessed = adoptions.stream()
                .filter(adoption -> adoption.getActiveState() == ActiveState.ACTIVE
                        && !adoption.isAiProcessed())
                .toList();

        log.info("AI로 {} 개의 활성 입양 정보 처리 중", activeNotProcessed.size());

        for (int i = 0; i < activeNotProcessed.size(); i += batchSize) {
            int end = Math.min(i + batchSize, activeNotProcessed.size());
            List<Adoption> batch = activeNotProcessed.subList(i, end);

            processBatch(batch);
        }
    }

    private void processBatch(List<Adoption> batch) {
        List<String> specialMarks = batch.stream()
                .map(Adoption::extractRefinedSpecialMark)
                .collect(Collectors.toList());

        List<String> tags = batch.stream()
                .map(Adoption::extractTagsField)
                .collect(Collectors.toList());

        List<Optional<String>> refinedSpecialMarks = adoptionAiPort.refineSpecialMarkBatch(
                specialMarks);
        List<Optional<List<String>>> tagsFields = adoptionAiPort.tagBatch(tags);

        List<Adoption> toUpdate = new ArrayList<>();

        for (int i = 0; i < batch.size(); i++) {
            Adoption adoption = batch.get(i);
//            log.info("AdoptionId = {}", adoption.getAdoptionId());

            String refinedSpecialMark = refinedSpecialMarks.get(i).orElse("");
            List<String> tagsList = tagsFields.get(i).orElse(Collections.emptyList());
            String tagsField = String.join(",", tagsList);

            if (isAiFieldChanged(adoption, refinedSpecialMark, tagsField)) {
                adoption.updateAiField(refinedSpecialMark, tagsField);
                toUpdate.add(adoption);
            }
        }

        if (!toUpdate.isEmpty()) {
            adoptionDataCommandPort.updateAiFields(toUpdate);
        }
    }

    private boolean isAiFieldChanged(Adoption adoption, String refinedSpecialMark, String tagsField) {
        boolean aiProcessed = (refinedSpecialMark != null && !refinedSpecialMark.isBlank()) ||
                (tagsField != null && !tagsField.isBlank());

        // Objects.equals()를 사용하여 null 안전 비교
        return aiProcessed || !Objects.equals(adoption.getRefinedSpecialMark(), refinedSpecialMark)
                || !Objects.equals(adoption.getTagsField(), tagsField);
    }
}

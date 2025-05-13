package kr.co.pawong.pwbe.adoption.application.service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import kr.co.pawong.pwbe.adoption.application.port.in.UpdateAdoptionDataUseCase;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionCreate;
import kr.co.pawong.pwbe.adoption.application.port.out.AdoptionAiPort;
import kr.co.pawong.pwbe.adoption.application.port.out.AdoptionDataCommandPort;
import kr.co.pawong.pwbe.adoption.application.port.out.AdoptionDataQueryPort;
import kr.co.pawong.pwbe.adoption.domain.model.Adoption;
import kr.co.pawong.pwbe.adoption.enums.ActiveState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateAdoptionDataService implements UpdateAdoptionDataUseCase {

    private final AdoptionDataCommandPort adoptionDataCommandPort;
    private final AdoptionDataQueryPort adoptionDataQueryPort;
    private final AdoptionAiPort adoptionAiPort;

    @Value("${adoption.batch-size:50}")
    private int batchSize;

    // AdoptionCreate -> Adoption -> Repo에 전달
    @Transactional
    @Override
    public void saveAdoptions(List<AdoptionCreate> adoptionCreates) {
        int updatedCount = 0;
        int insertedCount = 0;

        for (AdoptionCreate adoptionCreate : adoptionCreates) {
            Optional<Adoption> existingAdoption = duplicateAdoption(adoptionCreate.getDesertionNo());

            if (existingAdoption.isPresent()) {
                Adoption adoption = existingAdoption.get();

                if (adoption.getUpdTm() == null ||
                adoptionCreate.getUpdTm() == null ||
                adoption.getUpdTm().isBefore(adoptionCreate.getUpdTm())) {

                    Adoption updatedAdoption = Adoption.from(adoptionCreate);

                    updatedAdoption.setId(adoption.getAdoptionId());

                    adoptionDataCommandPort.updateAdoption(updatedAdoption);
                    updatedCount++;
//                    log.info("유기동물 정보 업데이트: {}", adoptionCreate.getDesertionNo());
                }
            } else {
                Adoption newAdoption = Adoption.from(adoptionCreate);

                adoptionDataCommandPort.saveAdoption(newAdoption);
                insertedCount++;
//                log.info("새로운 유기동물 정보 저장: {}", adoptionCreate.getDesertionNo());
            }
        }

        log.info("데이터 처리 완료: {} 건 삽입, {} 건 업데이트", insertedCount, updatedCount);
    }

    @Transactional
    public Optional<Adoption> duplicateAdoption(String desertionNo) {
        List<Adoption> duplicateAdoptions = adoptionDataQueryPort.findAllByDesertionNo(desertionNo);

        if (duplicateAdoptions.size() <= 1) {
            return duplicateAdoptions.isEmpty() ? Optional.empty() : Optional.of(duplicateAdoptions.get(0));
        }

        log.info("중복된 유기동물 정보 발견: {}, 개수: {}", desertionNo, duplicateAdoptions.size());

        Adoption adoptionToKeep = findAdoptionToKeep(duplicateAdoptions);

        int removedCount = 0;
        for (Adoption adoption : duplicateAdoptions) {
            if (!adoption.getAdoptionId().equals(adoptionToKeep.getAdoptionId())) {
                adoptionDataCommandPort.deleteAdoption(adoption);
                removedCount++;
            }
        }

        log.info("중복 데이터 처리 완료: {}, 삭제된 데이터 수: {}", desertionNo, removedCount);

        return Optional.of(adoptionToKeep);
    }

    private Adoption findAdoptionToKeep(List<Adoption> duplicateAdoptions) {
        return duplicateAdoptions.stream()
                .min(Comparator.comparing(Adoption::getAdoptionId))
                .orElse(duplicateAdoptions.get(0));
    }

    /**
     * DB에서 Adoption 도메인 객체를 모두 조회하여 ACTIVE 상태만 AI 전처리(정제) 후 refinedSpecialMark, tagsField,
     * aiProcessed가 변경된 경우만 50개씩 정제하고 50개씩 업데이트
     */
    @Override
    public void aiProcessAdoptions() {
        List<Adoption> adoptions = adoptionDataQueryPort.findAll();

        List<Adoption> activeNotProcessed = adoptions.stream()
                .filter(adoption -> adoption.getActiveState() == ActiveState.ADOPTED
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

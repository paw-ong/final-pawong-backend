package kr.co.pawong.pwbe.adoption.application.service;

import jakarta.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import kr.co.pawong.pwbe.adoption.application.port.in.CommandAdoptionDataUseCase;
import kr.co.pawong.pwbe.adoption.enums.ActiveState;
import kr.co.pawong.pwbe.infrastructure.api.dto.AdoptionCreate;
import kr.co.pawong.pwbe.adoption.application.port.out.AdoptionAiPort;
import kr.co.pawong.pwbe.adoption.application.port.out.AdoptionDataCommandPort;
import kr.co.pawong.pwbe.adoption.application.port.out.AdoptionDataQueryPort;
import kr.co.pawong.pwbe.adoption.domain.model.Adoption;
import kr.co.pawong.pwbe.lostPost.application.port.in.PublishCreatedLostAnimalUseCase;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommandAdoptionDataService implements CommandAdoptionDataUseCase {

    private final AdoptionDataCommandPort adoptionDataCommandPort;
    private final AdoptionDataQueryPort adoptionDataQueryPort;
    private final AdoptionAiPort adoptionAiPort;

    private final PublishCreatedLostAnimalUseCase publishCreatedLostAnimalUseCase;

    // Adoption 정보 저장
    @Transactional
    @Override
    public void saveAdoptions(List<AdoptionCreate> adoptionCreates) {
        int updatedCount = 0;
        int insertedCount = 0;

        for (AdoptionCreate adoptionCreate : adoptionCreates) {
            // 중복 데이터 확인
            Optional<Adoption> existingAdoption = duplicateAdoption(adoptionCreate.getDesertionNo());

            long idForMissing;
            Adoption adoptionForMissing;
            if (existingAdoption.isPresent()) {
                Adoption adoption = existingAdoption.get();

                // Adoption 데이터가 변경되었으면 업데이트
                if (isAdoptionUpdated(adoption, adoptionCreate)) {
                    Adoption updatedAdoption = Adoption.from(adoptionCreate);
                    updatedAdoption.initId(adoption.getAdoptionId());

                    adoptionDataCommandPort.updateAdoption(updatedAdoption);
                    updatedCount++;

                    if (adoption.getActiveState() == ActiveState.MISSING
                            && updatedAdoption.getActiveState() != ActiveState.MISSING) {
                        // TODO: ES_에서 해당 데이터 삭제해야됨. (이전에 실종 동물이었는데 바뀐 경우)
                    } else {
                        // MISSING 동물의 경우 메시지 발행
                        publishMissingAdoption(adoption.getAdoptionId(), updatedAdoption);
                    }
                }
            } else {
                // 새로운 데이터 저장
                Adoption newAdoption = Adoption.from(adoptionCreate);

                long newId = adoptionDataCommandPort.saveAdoption(newAdoption);
                insertedCount++;

                // MISSING 동물의 경우 메시지 발행
                publishMissingAdoption(newId, newAdoption);
            }
        }

        log.info("데이터 처리 완료: {} 건 삽입, {} 건 업데이트", insertedCount, updatedCount);
    }

    private void publishMissingAdoption(long id, Adoption adoption) {
        if (adoption.getActiveState() == ActiveState.MISSING) {
            // 특징 문자열 생성
            String textFeature = String.join(" ",
                    adoption.getColorCd(),
                    adoption.getKindNm(),
                    adoption.getUpKindNm().name());
            // 구조 동물 추가됐다는 메시지 발행
            publishCreatedLostAnimalUseCase.publishCreatedLostAnimal(
                    id,
                    PostType.FOSTER,
                    textFeature,
                    adoption.getPopfile1()
            );
        }
    }

    // Adoption 데이터 업데이트 여부 확인
    private boolean isAdoptionUpdated(Adoption existingAdoption, AdoptionCreate adoptionCreate) {
        return existingAdoption.getUpdTm() == null ||
                adoptionCreate.getUpdTm() == null ||
                existingAdoption.getUpdTm().isBefore(adoptionCreate.getUpdTm());
    }

    // 중복되는 adoption 정보가 있는지 확인
    private Optional<Adoption> duplicateAdoption(String desertionNo) {
        // 동일한 desertionNo를 가진 모든 데이터 조회
        List<Adoption> duplicateAdoptions = adoptionDataQueryPort.findAllByDesertionNo(desertionNo);

        // 중복 데이터가 없거나 하나만 있는 경우
        if (duplicateAdoptions.size() <= 1) {
            return duplicateAdoptions.isEmpty() ? Optional.empty() : Optional.of(duplicateAdoptions.get(0));
        }

        log.info("중복된 유기동물 정보 발견: {}, 개수: {}", desertionNo, duplicateAdoptions.size());

        // 유지할 데이터 선택
        Adoption adoptionToKeep = findAdoptionToKeep(duplicateAdoptions);

        int removedCount = 0;
        for (Adoption adoption : duplicateAdoptions) {
            // 중복 데이터가 있으면 하나만 남기고 제거
            if (!adoption.getAdoptionId().equals(adoptionToKeep.getAdoptionId())) {
                adoptionDataCommandPort.deleteAdoption(adoption);
                removedCount++;
            }
        }

        log.info("중복 데이터 처리 완료: {}, 삭제된 데이터 수: {}", desertionNo, removedCount);

        return Optional.of(adoptionToKeep);
    }

    // 중복 데이터 중 유지할 데이터 선택
    private Adoption findAdoptionToKeep(List<Adoption> duplicateAdoptions) {
        return duplicateAdoptions.stream()
                // adoptionId가 가장 작은(가장 먼저 생성된) 데이터 유지
                .min(Comparator.comparing(Adoption::getAdoptionId))
                .orElse(duplicateAdoptions.get(0));
    }

    // Adoption 데이터 AI 정제
    @Override
    public Adoption processAdoption(Adoption adoption) {
        // specialMark, tag 추출
        String specialMark = adoption.extractRefinedSpecialMark();
        String tags = adoption.extractTagsField();

        // AI를 이용한 specialMark, tag 정제
        String refinedSpecialMark = adoptionAiPort.refineSpecialMark(specialMark);
        List<String> tagsList = adoptionAiPort.tag(tags);
        // 태그 목록을 문자열로 반환
        String tagsField = String.join(",", tagsList);

        // AI 필드가 변경되었으면 업데이트
        if (isAiFieldChanged(adoption, refinedSpecialMark, tagsField)) {
            adoption.updateAiField(refinedSpecialMark, tagsField);
            return adoption;
        }

        // 변경되지 않은 경우 null 반환
        return null;
    }

    // AI 정제 필드 변경 여부 확인
    private boolean isAiFieldChanged(Adoption adoption, String refinedSpecialMark, String tagsField) {
        // AI 처리 여부 확인
        boolean aiProcessed = (refinedSpecialMark != null && !refinedSpecialMark.isBlank()) ||
                (tagsField != null && !tagsField.isBlank());

        // 기존 값과 새 값이 다른 경우 변경된 것으로 판단
        return aiProcessed || !Objects.equals(adoption.getRefinedSpecialMark(), refinedSpecialMark)
                || !Objects.equals(adoption.getTagsField(), tagsField);
    }
}

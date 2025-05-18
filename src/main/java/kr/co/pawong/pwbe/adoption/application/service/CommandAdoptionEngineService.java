package kr.co.pawong.pwbe.adoption.application.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kr.co.pawong.pwbe.adoption.application.port.in.CommandAdoptionEngineUseCase;
import kr.co.pawong.pwbe.adoption.application.port.in.QueryAdoptionDataUseCase;
import kr.co.pawong.pwbe.adoption.application.port.out.AdoptionAiPort;
import kr.co.pawong.pwbe.adoption.application.port.out.AdoptionDataCommandPort;
import kr.co.pawong.pwbe.adoption.application.port.out.AdoptionEngineCommandPort;
import kr.co.pawong.pwbe.adoption.application.port.out.dto.AdoptionEsDto;
import kr.co.pawong.pwbe.adoption.application.port.out.dto.RegionInfoDto;
import kr.co.pawong.pwbe.adoption.domain.model.Adoption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommandAdoptionEngineService implements CommandAdoptionEngineUseCase {

    private final AdoptionEngineCommandPort adoptionEngineCommandPort;
    private final AdoptionDataCommandPort adoptionDataCommandPort;
    private final AdoptionAiPort adoptionAiPort;
    private final QueryAdoptionDataUseCase queryAdoptionDataUseCase;

    // 임베딩
    @Override
    public AdoptionEsDto processAdoptionForEs(Adoption adoption) {
        try {
            // 임베딩 필드 생성
            String combinedField = Stream.of(adoption.getRefinedSpecialMark(),
                            adoption.getTagsField())
                    .filter(field -> field != null && !field.isBlank())
                    .collect(Collectors.joining(","));

            // 임베딩할 내용이 없는 경우 null 반환
            if (combinedField.isBlank()) {
                log.warn("Adoption ID {}: 임베딩할 필드가 없습니다.", adoption.getAdoptionId());
                return null;
            }

            float[] embedding = adoptionAiPort.embed(combinedField);

            // 임베딩 성공 시 adoption 세팅
            if (embedding != null && embedding.length > 0) {
                adoption.embed(embedding);
            } else {
                log.error("Adoption ID {}: 임베딩에 실패했습니다.", adoption.getAdoptionId());
                return null;
            }

            // 지역 정보 조회 및 DTO 생성
            RegionInfoDto regionInfoDto = RegionInfoDto.from(
                    queryAdoptionDataUseCase.findShelterInfoByAdoptionId(
                            adoption.getAdoptionId()));

            return AdoptionEsDto.from(adoption, regionInfoDto);
        } catch (Exception e) {
            log.error("Error processing adoption ID {}: {}", adoption.getAdoptionId(),
                    e.getMessage());
            return null;
        }
    }

    // AdoptionEsDto -> ES에 저장, RDB -> IsEmbedded 업데이트
    @Override
    public void saveEsAndUpdateIsEmbedded(List<AdoptionEsDto> adoptionEsDtos) {
        if (adoptionEsDtos.isEmpty()) {
            return;
        }

        try {
            log.info("ES에 저장 시작: {} 개의 아이템", adoptionEsDtos.size());
            adoptionEngineCommandPort.saveAdoptionToEs(adoptionEsDtos);
            log.info("ES에 저장 완료");

            // 처리된 ID 목록 수집
            List<Long> processedIds = adoptionEsDtos.stream()
                    .map(AdoptionEsDto::getAdoptionId)
                    .toList();

            // 임베딩 완료 상태를 RDB에 업데이트
            adoptionDataCommandPort.updateIsEmbeddedByIds(processedIds);
        } catch (Exception e) {
            log.error("Elasticsearch 저장 중 오류 발생: {}", e.getMessage(), e);
        }
    }
}

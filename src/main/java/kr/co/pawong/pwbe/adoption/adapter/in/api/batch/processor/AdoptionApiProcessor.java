package kr.co.pawong.pwbe.adoption.adapter.in.api.batch.processor;

import kr.co.pawong.pwbe.adoption.application.port.in.ApiRequestUseCase;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionCreate;
import kr.co.pawong.pwbe.adoption.application.service.dto.AdoptionApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdoptionApiProcessor implements ItemProcessor<AdoptionApi.Item, AdoptionCreate> {
    private final ApiRequestUseCase apiRequestUseCase;

    @Override
    public AdoptionCreate process(AdoptionApi.Item item) {
//        // 보호소 정보 추출 및 처리
//        apiRequestUseCase.extractAndProcessShelterInfo(item);
//        log.debug("보호소 정보 처리 완료: careRegNo={}", item.getCareRegNo());

        return apiRequestUseCase.convertToAdoptionCreate(item);
    }
}

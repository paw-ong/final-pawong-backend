package kr.co.pawong.pwbe.adoption.adapter.out.batch.processor;

import java.util.Collections;
import java.util.List;
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
        // 데이터 전처리
        List<AdoptionCreate> adoptionCreates = apiRequestUseCase.convertToAdoptionCreates(
                Collections.singletonList(item));

        // 변환된 결과가 있으면 첫 번째 항목 반환, 없으면 null 반환
        return adoptionCreates.isEmpty() ? null : adoptionCreates.get(0);
    }
}

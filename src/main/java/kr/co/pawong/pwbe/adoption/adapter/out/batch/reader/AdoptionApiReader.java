package kr.co.pawong.pwbe.adoption.adapter.out.batch.reader;

import kr.co.pawong.pwbe.adoption.application.port.in.ApiRequestUseCase;
import kr.co.pawong.pwbe.adoption.application.service.dto.AdoptionApi;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Component
@StepScope
@RequiredArgsConstructor
public class AdoptionApiReader implements ItemReader<AdoptionApi.Item> {
    private final ApiRequestUseCase apiRequestUseCase;

    @Override
    public AdoptionApi.Item read() {
        return apiRequestUseCase.fetchNextAdoptionItem();
    }
}

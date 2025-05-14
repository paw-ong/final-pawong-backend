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

    // test
    private int count = 0;  // 처리한 아이템 수 카운트
    private final int maxCount = 50;  // 최대 처리 아이템 수


    @Override
    public AdoptionApi.Item read() {
        if (count >= maxCount) {
            return null;  // 150개 이상 처리 시 null 반환하여 종료
        }

        // 공공데이터 조회
        AdoptionApi.Item item = apiRequestUseCase.fetchNextAdoptionItem();
        if (item != null) {
            count++;
        }

        return item;
    }
}

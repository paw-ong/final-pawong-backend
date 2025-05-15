package kr.co.pawong.pwbe.adoption.adapter.out.batch.reader;

import java.util.Iterator;
import java.util.List;
import kr.co.pawong.pwbe.adoption.application.port.in.QueryAdoptionDataUseCase;
import kr.co.pawong.pwbe.adoption.domain.model.Adoption;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Component
@StepScope
@RequiredArgsConstructor
public class AdoptionAiReader implements ItemReader<Adoption> {
    private final QueryAdoptionDataUseCase queryAdoptionDataUseCase;
    private Iterator<Adoption> adoptionsIterator;
    private boolean initialized = false;
    // 테스트용
    private int count = 0;  // 처리한 아이템 수 카운트
    private final int maxCount = 50;  // 최대 처리 아이템 수

    @Override
    public Adoption read(){
        if (!initialized) {
            // AI 정제할 adoption 조회
            List<Adoption> adoptions = queryAdoptionDataUseCase.findActiveNotProcessedAdoptions();
            adoptionsIterator = adoptions.iterator();
            initialized = true;
        }

        if (count >= maxCount) {
            return null;  // 150개 이상 처리 시 null 반환하여 종료
        }

        if (adoptionsIterator.hasNext()) {
            count++;
            return adoptionsIterator.next();
        } else {
            return null;
        }
//        return adoptionsIterator.hasNext() ? adoptionsIterator.next() : null;
    }
}

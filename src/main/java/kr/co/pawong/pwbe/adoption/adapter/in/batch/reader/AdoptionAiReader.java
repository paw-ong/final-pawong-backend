package kr.co.pawong.pwbe.adoption.adapter.in.batch.reader;

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

    @Override
    public Adoption read() {
        if (!initialized) {
            // AI 정제할 adoption 조회
            List<Adoption> adoptions = queryAdoptionDataUseCase.findActiveNotProcessedAdoptions();
            adoptionsIterator = adoptions.iterator();
            initialized = true;
        }
        return adoptionsIterator.hasNext() ? adoptionsIterator.next() : null;
    }
}

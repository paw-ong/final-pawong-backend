package kr.co.pawong.pwbe.adoption.adapter.in.api.batch.reader;

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
public class AdoptionEsReader implements ItemReader<Adoption> {
    private final QueryAdoptionDataUseCase queryAdoptionDataUseCase;
    // 조회한 입양 데이터를 순차적으로 접근하기 위한 반복자 -> 다음 요소를 반환하는 데 사용
    private Iterator<Adoption> adoptionsIterator;
    // Reader 초기화 여부
    private boolean initialized = false;

    @Override
    public Adoption read() {
        if (!initialized) {
            // 임베딩할 adoption 조회
            List<Adoption> adoptions = queryAdoptionDataUseCase.findAdoptionForEmbedding();
            adoptionsIterator = adoptions.iterator();
            initialized = true;
        }
        return adoptionsIterator.hasNext() ? adoptionsIterator.next() : null;
    }
}

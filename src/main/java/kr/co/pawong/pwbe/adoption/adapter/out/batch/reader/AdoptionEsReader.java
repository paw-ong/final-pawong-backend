package kr.co.pawong.pwbe.adoption.adapter.out.batch.reader;

import java.util.Iterator;
import java.util.List;
import kr.co.pawong.pwbe.adoption.application.port.in.QueryAdoptionDataUseCase;
import kr.co.pawong.pwbe.adoption.domain.model.Adoption;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AdoptionEsReader implements ItemReader<Adoption> {
    private final QueryAdoptionDataUseCase queryAdoptionDataUseCase;
    private Iterator<Adoption> adoptionsIterator;
    private boolean initialized = false;
    // 테스트용
    private int count = 0;  // 처리한 아이템 수 카운트
    private final int maxCount = 50;  // 최대 처리 아이템 수


    @Override
    public Adoption read() {
        if (!initialized) {
            // 임베딩할 adoption 조회
            List<Adoption> adoptions = queryAdoptionDataUseCase.findAdoptionForEmbedding();
            adoptionsIterator = adoptions.iterator();
            initialized = true;
        }

        if (count >= maxCount) {
            return null;
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

package kr.co.pawong.pwbe.adoption.adapter.out.batch.writer;

import java.util.ArrayList;
import java.util.List;
import kr.co.pawong.pwbe.adoption.application.port.out.AdoptionDataCommandPort;
import kr.co.pawong.pwbe.adoption.domain.model.Adoption;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdoptionAiWriter implements ItemWriter<Adoption> {
    private final AdoptionDataCommandPort adoptionDataCommandPort;

    @Override
    public void write(Chunk<? extends Adoption> chunk) {
        List<Adoption> items = new ArrayList<>(chunk.getItems());
        if (!items.isEmpty()) {
            // AI 필드 업데이트
            adoptionDataCommandPort.updateAiFields(items);
        }
    }
}

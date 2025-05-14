package kr.co.pawong.pwbe.adoption.adapter.out.batch.writer;

import java.util.ArrayList;
import java.util.List;
import kr.co.pawong.pwbe.adoption.application.port.in.CommandAdoptionDataUseCase;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdoptionApiWriter implements ItemWriter<AdoptionCreate> {
    private final CommandAdoptionDataUseCase commandAdoptionDataUseCase;

    @Override
    public void write(Chunk<? extends AdoptionCreate> chunk) {
        List<AdoptionCreate> items = new ArrayList<>(chunk.getItems());
        // 새로운 정보는 저장, 있는 정보는 변경 사항이 있으면 업데이트
        commandAdoptionDataUseCase.saveAdoptions(items);
    }
}

package kr.co.pawong.pwbe.adoption.adapter.out.batch.processor;

import kr.co.pawong.pwbe.adoption.application.port.in.CommandAdoptionEngineUseCase;
import kr.co.pawong.pwbe.adoption.application.port.out.dto.AdoptionEsDto;
import kr.co.pawong.pwbe.adoption.domain.model.Adoption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AdoptionEsProcessor implements ItemProcessor<Adoption, AdoptionEsDto> {
    private final CommandAdoptionEngineUseCase commandAdoptionEngineUseCase;

    @Override
    public AdoptionEsDto process(Adoption adoption) {
        // 임베딩 + 지역 정보
        return commandAdoptionEngineUseCase.processAdoptionForEs(adoption);
    }
}

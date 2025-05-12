package kr.co.pawong.pwbe.adoption.application.port.out;

import java.util.List;
import kr.co.pawong.pwbe.adoption.domain.model.Adoption;

public interface AdoptionDataCommandPort {
    // 유기동물정보 RDB에 저장
    void saveAdoptions(List<Adoption> adoptions);

    // EmbeddingDone 업데이트
    void updateIsEmbedded(List<Adoption> adoptions);

    // Ai 관련 데이터 업데이트
    void updateAiFields(List<Adoption> adoptions);
}

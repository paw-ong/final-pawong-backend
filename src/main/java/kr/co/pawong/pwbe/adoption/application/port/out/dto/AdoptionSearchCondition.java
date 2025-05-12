package kr.co.pawong.pwbe.adoption.application.port.out.dto;

import kr.co.pawong.pwbe.adoption.enums.NeuterYn;
import kr.co.pawong.pwbe.adoption.enums.SexCd;
import kr.co.pawong.pwbe.adoption.enums.UpKindCd;
import lombok.*;

import java.util.List;

@Getter
@Builder
// 실제 es 검색에 활용될 검색 조건
public class AdoptionSearchCondition {
    private List<UpKindCd> upKindCds;
    private SexCd sexCd;
    private NeuterYn neuterYn;
    private List<Region> regions;
    private String refinedSearchTerm;   // 정제된 검색어 문장
    private List<String> tags;
    private float[] embedding;

    /** city/district 를 담는 DTO **/
    @Getter
    @AllArgsConstructor
    public static class Region {
        private final String city;
        private final String district;
    }
}

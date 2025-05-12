package kr.co.pawong.pwbe.adoption.application.service.support;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kr.co.pawong.pwbe.adoption.domain.model.Adoption;
import kr.co.pawong.pwbe.adoption.application.port.out.dto.AdoptionSearchCondition;
import kr.co.pawong.pwbe.adoption.application.port.out.dto.AdoptionSearchCondition.Region;
import kr.co.pawong.pwbe.adoption.adapter.in.api.dto.request.AdoptionSearchRequest;
import kr.co.pawong.pwbe.adoption.application.port.in.dto.AdoptionIdSearchResponse;

public class AdoptionSearchMapper {

    public static AdoptionSearchCondition fromRequest(AdoptionSearchRequest request, String refinedSearchTerm, List<String> tags, float[] embedding) {
        return AdoptionSearchCondition.builder()
                .upKindCds(request.getUpKindCds())
                .sexCd(request.getSexCd())
                .neuterYn(request.getNeuterYn())
                .regions(toRegionList(request.getRegions()))
                .refinedSearchTerm(refinedSearchTerm)
                .tags(tags)
                .embedding(embedding)
                .build();
    }

    // ES에 adoptionId lookup을 위한 mapping
    public static AdoptionIdSearchResponse toIdSearchResponse(Adoption model) {
        return AdoptionIdSearchResponse.builder()
                .adoptionId(model.getAdoptionId())
                .build();
    }

    /**
     * regions 문자열을 city/district 로 파싱한 리스트를 반환합니다.
     * - "서울특별시"        → city="서울특별시", district=null
     * - "인천광역시 부평구" → city="인천광역시", district="부평구"
     */
    public static List<Region> toRegionList(List<String> regions) {
        if (regions == null) {
            return Collections.emptyList();
        }
        return regions.stream()
            .map(AdoptionSearchMapper::parseRegion)
            .flatMap(Optional::stream)
            .toList();
    }
    public static Optional<Region> parseRegion(String cityAndDistrict) {
        if(cityAndDistrict == null || cityAndDistrict.isBlank())
            return Optional.empty();
        String trimmed = cityAndDistrict.trim();
        int idx = trimmed.indexOf(' ');
        Region region = (idx < 0)
            ? new Region(trimmed, null)
            : new Region(trimmed.substring(0, idx), trimmed.substring(idx + 1));
        return Optional.of(region);
    }


}

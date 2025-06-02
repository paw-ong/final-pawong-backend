package kr.co.pawong.pwbe.lostPost.application.port.out.dto;

import kr.co.pawong.pwbe.adoption.enums.SexCd;
import kr.co.pawong.pwbe.adoption.enums.UpKindCd;
import kr.co.pawong.pwbe.lostPost.domain.LostAdoption;
import kr.co.pawong.pwbe.lostPost.domain.LostGeoPoint;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LostAnimalDto {
    private String lostAnimalId;
    private Long rdbId;
    private String type;
    private UpKindCd upKindCd;
    private SexCd sexCd;
    private String city;
    private String district;
    private LostGeoPoint lostGeoPoint;
    private float[] embedding;

    public static LostAnimalDto fromLostPost(LostPost lostPost, PostType type, float[] embedding) {
        // 공백으로 분리 (앞의 두 단어만 사용)
        String[] locationParts = lostPost.getLocation().trim().split("\\s+");

        String city = locationParts.length > 0 ? locationParts[0] : "";
        String district = locationParts.length > 1 ? locationParts[1] : "";

        return LostAnimalDto.builder()
                .lostAnimalId(type.name() + "_" + lostPost.getLostPostId())
                .rdbId(lostPost.getLostPostId())
                .type(type.name())
                .upKindCd(lostPost.getUpKindCd())
                .sexCd(lostPost.getSexCd())
                .city(city)
                .district(district)
                .lostGeoPoint(lostPost.getLostGeoPoint())
                .embedding(embedding)
                .build();
    }

    public static LostAnimalDto fromLostAnimal(LostAdoption lostAdoption, PostType type, float[] embedding) {
        return LostAnimalDto.builder()
                .lostAnimalId(type.name() + "_" + lostAdoption.getAdoptionId())
                .rdbId(lostAdoption.getAdoptionId())
                .type(type.name())
                .upKindCd(lostAdoption.getUpKindCd())
                .sexCd(lostAdoption.getSexCd())
                .city(null)
                .district(null)
                .lostGeoPoint(null)
                .embedding(embedding)
                .build();
    }
}

package kr.co.pawong.pwbe.lostPost.adapter.out.persistence.es.document;

import kr.co.pawong.pwbe.adoption.enums.SexCd;
import kr.co.pawong.pwbe.adoption.enums.UpKindCd;
import kr.co.pawong.pwbe.lostPost.application.port.out.dto.LostAnimalDto;
import kr.co.pawong.pwbe.lostPost.domain.LostGeoPoint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Slf4j
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "lost_animal")
public class LostAnimalDocument {

    @Id
    @Field(type = FieldType.Keyword, name = "lostAnimalId")
    private String lostAnimalId; // type + id // ex) LOST_13, FOUND_243

    @Field(type = FieldType.Long, name = "rdbId")
    private Long rdbId; // id

    @Field(type = FieldType.Keyword, name = "type")
    private String type; // LOST, FOUND, RESQUE

    @Field(type = FieldType.Keyword, name = "upKindCd")
    private UpKindCd upKindCd;

    @Field(type = FieldType.Keyword, name = "sexCd")
    private SexCd sexCd;

    @Field(type = FieldType.Keyword, name = "city")
    private String city;

    @Field(type = FieldType.Keyword, name = "district")
    private String district;

    @GeoPointField
    @Field(name = "geoPoint")
    private GeoPoint geoPoint;

    @Field(type = FieldType.Dense_Vector, dims = 512, name = "embedding")
    private float[] embedding;

    public static LostAnimalDocument from(LostAnimalDto lostAnimalDto) {
        GeoPoint geoPoint = null;
        if (lostAnimalDto.getLostGeoPoint() != null) {
            LostGeoPoint customGeoPoint = lostAnimalDto.getLostGeoPoint();
            geoPoint = new GeoPoint(
                    customGeoPoint.getLatitude().doubleValue(),
                    customGeoPoint.getLongitude().doubleValue()
            );
        }

        return LostAnimalDocument.builder()
                .lostAnimalId(lostAnimalDto.getLostAnimalId())
                .rdbId(lostAnimalDto.getRdbId())
                .type(lostAnimalDto.getType())
                .upKindCd(lostAnimalDto.getUpKindCd())
                .sexCd(lostAnimalDto.getSexCd())
                .city(lostAnimalDto.getCity())
                .district(lostAnimalDto.getDistrict())
                .geoPoint(geoPoint)
                .embedding(lostAnimalDto.getEmbedding())
                .build();
    }
}

package kr.co.pawong.pwbe.adoption.adapter.out.persistence.es.document;

import kr.co.pawong.pwbe.adoption.domain.model.Adoption;
import kr.co.pawong.pwbe.adoption.application.port.out.dto.AdoptionEsDto;
import kr.co.pawong.pwbe.adoption.enums.NeuterYn;
import kr.co.pawong.pwbe.adoption.enums.SexCd;
import kr.co.pawong.pwbe.adoption.enums.UpKindCd;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "adoption")   // lowercase
public class AdoptionDocument {
    @Id
    @Field(type = FieldType.Keyword, name = "adoptionId")
    private Long adoptionId; // 입양id

    @Field(type = FieldType.Keyword, name = "upKindCd")
    private UpKindCd upKindCd; // 축종코드

    @Field(type = FieldType.Keyword, name = "sexCd")
    private SexCd sexCd; // 성별

    @Field(type = FieldType.Keyword, name = "neuterYn")
    private NeuterYn neuterYn; // 중성화여부(타입)

    @Field(type = FieldType.Keyword, name = "city")
    private String city; // 시도

    @Field(type = FieldType.Keyword, name = "district")
    private String district; // 시군구

    @Setter
    @Field(type = FieldType.Text, name = "refinedSpecialMark", analyzer = "korean")
    private String refinedSpecialMark; // AI가 정제한 동물 정보

    @Setter
    @Field(type = FieldType.Keyword, name = "tagsField")
    private List<String> tagsField; // AI가 키워드로 정제한 동물 정보

    @Setter
    @Field(type = FieldType.Dense_Vector, dims = 1536, name = "embedding")
    private float[] embedding; // 임베딩

    public static AdoptionDocument from(AdoptionEsDto adoptionEsDto) {
        return AdoptionDocument.builder()
                .adoptionId(adoptionEsDto.getAdoptionId())
                .upKindCd(adoptionEsDto.getUpKindCd())
                .sexCd(adoptionEsDto.getSexCd())
                .neuterYn(adoptionEsDto.getNeuterYn())
                .city(adoptionEsDto.getCity())
                .district(adoptionEsDto.getDistrict())
                .refinedSpecialMark(adoptionEsDto.getRefinedSpecialMark())
                .tagsField(adoptionEsDto.getTagsField())
                .embedding(adoptionEsDto.getEmbedding())
                .build();
    }

    public Adoption toModel() {
        return Adoption.builder()
                .adoptionId(this.adoptionId)
                .upKindCd(this.upKindCd)
                .sexCd(this.sexCd)
                .neuterYn(this.neuterYn)
                .refinedSpecialMark(this.refinedSpecialMark)
                .tagsField(String.join(",", this.tagsField))
                .embedding(this.embedding)
                .build();
    }
}

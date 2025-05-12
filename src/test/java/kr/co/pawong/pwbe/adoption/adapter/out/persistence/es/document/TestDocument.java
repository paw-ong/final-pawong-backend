package kr.co.pawong.pwbe.adoption.adapter.out.persistence.es.document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@AllArgsConstructor
@Document(indexName = "test")
public class TestDocument {
  @Id
  @Field(type = FieldType.Keyword, name = "id")
  private Long id;
  @Field(type = FieldType.Keyword, name = "city")
  private String city;
  @Field(type = FieldType.Keyword, name = "district")
  private String district;
}

package kr.co.pawong.pwbe.adoption.adapter.out.persistence.es.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "autocomplete")
public class AutocompleteDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text, name = "word")
    private String word;
}

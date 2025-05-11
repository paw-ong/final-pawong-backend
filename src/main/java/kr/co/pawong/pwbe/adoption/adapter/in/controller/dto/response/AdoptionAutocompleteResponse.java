package kr.co.pawong.pwbe.adoption.adapter.in.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AdoptionAutocompleteResponse {
    private List<String> autocompletes;
}

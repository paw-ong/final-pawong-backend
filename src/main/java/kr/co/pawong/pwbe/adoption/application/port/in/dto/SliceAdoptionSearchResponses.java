package kr.co.pawong.pwbe.adoption.application.port.in.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SliceAdoptionSearchResponses {

    private boolean hasNext; // 다음 페이지가 있는지 여부
    private List<AdoptionCard> adoptionCards;
}
package kr.co.pawong.pwbe.adoption.application.port.in.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdoptionRecommendResponses {
    private List<AdoptionCard> adoptionCards;
}

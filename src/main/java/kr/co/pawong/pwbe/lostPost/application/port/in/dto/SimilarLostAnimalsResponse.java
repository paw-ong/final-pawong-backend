package kr.co.pawong.pwbe.lostPost.application.port.in.dto;

import java.util.List;

public record SimilarLostAnimalsResponse(
        List<LostPostCard> lostAnimals
) {

}

package kr.co.pawong.pwbe.lostPost.application.port.out.dto;

import kr.co.pawong.pwbe.lostPost.enums.PostType;
import lombok.Builder;

@Builder
public record CreatedLostAnimalPublishDto(
        long id,
        PostType type,
        String queryText,
        String queryImageUrl
) {

}

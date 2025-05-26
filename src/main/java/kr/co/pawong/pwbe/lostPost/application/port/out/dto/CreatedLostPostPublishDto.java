package kr.co.pawong.pwbe.lostPost.application.port.out.dto;

import lombok.Builder;

@Builder
public record CreatedLostPostPublishDto(
        long id,
        String type,
        String queryText,
        String queryImageUrl
) {

}

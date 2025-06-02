package kr.co.pawong.pwbe.lostPost.adapter.in.messaging.dto;

import kr.co.pawong.pwbe.lostPost.enums.PostType;

public record EmbeddedLostAnimalConsumeDto(
        long id,
        PostType type,
        String queryText,
        String queryImageUrl,
        float[] embedding
) {

}

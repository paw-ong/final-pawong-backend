package kr.co.pawong.pwbe.lostPost.adapter.in.messaging.dto;

public record EmbeddedLostPostConsumeDto(
        long id,
        String type,
        String queryText,
        String queryImageUrl,
        float[] embedding
) {

}

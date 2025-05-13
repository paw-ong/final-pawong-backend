package kr.co.pawong.pwbe.user.application.port.in.dto;

import lombok.Builder;

@Builder
public record MyPageLostPostResponse(
        Long postId,
        String postType,
        String author,
        String happenedDate,
        String happenedPlace,
        String upKindNm,
        String kindNm,
        String createdAt,
        String feature
) {
}

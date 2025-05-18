package kr.co.pawong.pwbe.user.application.port.in.dto;

import lombok.Builder;

/**
 * 마이페이지 LostPost 목록 최종 응답 객체
 */
@Builder
public record MyPageLostPostResponse(
        Long postId,
        String postType,
        String author,
        String imageUrl,
        String happenedDate,
        String happenedPlace,
        String kindNm,
        String createdAt,
        String feature,
        boolean bookmarked
) {

}

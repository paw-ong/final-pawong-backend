package kr.co.pawong.pwbe.user.application.port.out.dto;

import lombok.Builder;

/**
 * LostPost 도메인에서 받아온 LostPost DTO를 User Service 계층으로 전달하기 위한 DTO
 */
@Builder
public record MyPageLostPostInfo(
        Long postId,
        String postType,
        String author,
        String imageUrl,
        String happenedDate,
        String happenedPlace,
        String upKindNm,
        String kindNm,
        String createdAt,
        String feature
) {

}

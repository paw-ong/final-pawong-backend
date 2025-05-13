package kr.co.pawong.pwbe.user.adapter.in.api.dto.response;

import java.time.LocalDate;

public record MyPageLostPost(
        Long postId,
        String postType,
        String happenedDate,
        String upKindNm,
        String kindNm,
        String sexCd,
        String createdAt,
        String specialMark
) {
}

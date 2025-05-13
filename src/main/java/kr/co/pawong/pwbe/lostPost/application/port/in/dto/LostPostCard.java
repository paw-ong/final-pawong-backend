package kr.co.pawong.pwbe.lostPost.application.port.in.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;

/**
 * 프론트 LostPost 카드에 표시되는 데이터 DTO 입니다.
 */
@Builder
public record LostPostCard(
        Long postId,
        String postType,
        String author,
        LocalDate happenedDate,
        String happenedPlace,
        String upKindNm,
        String kindNm,
        LocalDateTime createdAt,
        String feature
) {

}

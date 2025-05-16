package kr.co.pawong.pwbe.lostPost.application.port.in.dto;

import lombok.Builder;

/**
 * 프론트 LostPost 카드에 표시되는 데이터 DTO 입니다.
 *
 * @param postId        // LostPost Id
 * @param postType      // LOST, FOUND, FOSTER
 * @param author        // 작성자 닉네임
 * @param imageUrl      // 이미지 url
 * @param happenedDate  // 실종 일자 or 발견 일자 - yyyy-MM-dd 형식
 * @param happenedPlace // 실종 장소 or 발견 장소
 * @param kindNm        // 상세 품종
 * @param createdAt     // 글 작성 일자 - 3초 전, 3개월 전 형식
 * @param feature       // 동물 특징
 * @param bookmarked    // 북마크 여부. 비로그인 상태일 경우 false
 */
@Builder
public record LostPostCard(
        long postId,
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

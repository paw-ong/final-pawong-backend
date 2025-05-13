package kr.co.pawong.pwbe.lostPost.application.port.in.dto;

import lombok.Builder;

/**
 * 프론트 LostPost 카드에 표시되는 데이터 DTO 입니다.
 * @param postId // LostPost Id
 * @param postType // LOST, FOUND, FOSTER
 * @param author // 작성자 닉네임
 * @param happenedDate // 실종 일시 or 발견 일시 - yyyy-MM-dd 형식
 * @param happenedPlace // 실종 장소 or 발견 장소
 * @param upKindNm // 개, 고양이, 기타
 * @param kindNm // 상세 품종
 * @param createdAt // 글 작성 일자 - 3초 전, 3개월 전 형식
 * @param feature // 동물 특징
 */
@Builder
public record LostPostCard(
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

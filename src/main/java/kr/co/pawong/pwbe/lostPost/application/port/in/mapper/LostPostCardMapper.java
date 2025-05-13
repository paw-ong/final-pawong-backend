package kr.co.pawong.pwbe.lostPost.application.port.in.mapper;

import kr.co.pawong.pwbe.global.time.TimeUtils;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;

/**
 * LostPost 도메인 객체를 LostPostCard_로 변환하는 Mapper_입니다.
 */
public class LostPostCardMapper {

    /**
     * @param timeUtils - TimeUtils 빈. (이 함수를 static으로 만들기 위해 주입받게 했습니다.)
     * @param lostPost - 변환할 LostPost
     * @param author - 작성자 닉네임
     */
    public static LostPostCard toLostPostCard(
            TimeUtils timeUtils, LostPost lostPost, String author) {

        return LostPostCard.builder()
                .postId(lostPost.getLostPostId())
                .postType(lostPost.getPostType().name())
                .author(author)
                .happenedDate(TimeUtils.formatDate(lostPost.getDate()))
                .happenedPlace(lostPost.getLocation())
                .upKindNm(lostPost.getUpKindNm().name())
                .kindNm(lostPost.getKindNm())
                .createdAt(timeUtils.formatTimeAgo(lostPost.getCreatedAt()))
                .feature(lostPost.getSpecialMark())
                .build();
    }
}

package kr.co.pawong.pwbe.lostPost.application.port.in.mapper;

import java.time.Clock;
import kr.co.pawong.pwbe.global.util.TimeUtils;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;

/**
 * LostPost 도메인 객체를 LostPostCard_로 변환하는 Mapper_입니다.
 */
public class LostPostCardMapper {

    /**
     * @param lostPost - 변환할 LostPost
     * @param author   - 작성자 닉네임
     */
    public static LostPostCard toLostPostCard(
            LostPost lostPost, String author, Clock clock) {

        return LostPostCard.builder()
                .postId(lostPost.getLostPostId())
                .postType(lostPost.getPostType().name())
                .author(author)
                .imageUrl(lostPost.getImageUrl())
                .happenedDate(TimeUtils.formatDate(lostPost.getDate()))
                .happenedPlace(lostPost.getLocation())
                .kindNm(lostPost.getKindNm())
                .createdAt(TimeUtils.formatTimeAgo(lostPost.getCreatedAt(), clock))
                .feature(lostPost.getSpecialMark())
                .build();
    }
}

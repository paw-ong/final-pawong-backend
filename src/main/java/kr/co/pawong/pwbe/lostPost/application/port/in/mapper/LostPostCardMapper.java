package kr.co.pawong.pwbe.lostPost.application.port.in.mapper;

import java.net.URL;
import java.time.Clock;
import kr.co.pawong.pwbe.global.util.TimeUtils;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import kr.co.pawong.pwbe.lostPost.domain.LostAdoption;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import kr.co.pawong.pwbe.lostPost.enums.PostType;

/**
 * LostPost 도메인 객체를 LostPostCard_로 변환하는 Mapper_입니다.
 */
public class LostPostCardMapper {

    /**
     * @param lostPost - 변환할 LostPost
     * @param author   - 작성자 닉네임
     */
    public static LostPostCard toLostPostCard(
            LostPost lostPost, String author, Clock clock, String url) {

        return LostPostCard.builder()
                .postId(lostPost.getLostPostId())
                .postType(lostPost.getPostType().name())
                .author(author)
                .imageUrl(url)
                .happenedDate(TimeUtils.formatDate(lostPost.getDate()))
                .happenedPlace(lostPost.getLocation())
                .kindNm(lostPost.getKindNm())
                .createdAt(TimeUtils.formatTimeAgo(lostPost.getCreatedAt(), clock))
                .feature(lostPost.getSpecialMark())
                .build();
    }

    /**
     * @param lostAdoption - 변환할 LostAdoption
     * @param shelter   - 작성자 닉네임
     */
    public static LostPostCard toLostPostCard(
            LostAdoption lostAdoption, String shelter, Clock clock) {

        return LostPostCard.builder()
                .postId(lostAdoption.getAdoptionId())
                .postType(PostType.FOSTER.name())
                .author(shelter)
                .imageUrl(lostAdoption.getPopfile1())
                .happenedDate(TimeUtils.formatDate(lostAdoption.getHappenDt()))
                .happenedPlace(lostAdoption.getHappenPlace())
                .kindNm(lostAdoption.getKindNm())
                .createdAt(TimeUtils.formatDateAgo(lostAdoption.getNoticeSdt(), clock))
                .feature(lostAdoption.getSpecialMark())
                .build();
    }

}

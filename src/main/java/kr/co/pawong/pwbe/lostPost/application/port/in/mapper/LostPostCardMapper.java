package kr.co.pawong.pwbe.lostPost.application.port.in.mapper;

import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;

public class LostPostCardMapper {

    public static LostPostCard toLostPostCard(LostPost lostPost, String author) {

        return LostPostCard.builder()
                .postId(lostPost.getLostPostId())
                .postType(lostPost.getPostType().name())
                .author(author)
                .happenedDate(lostPost.getDate())
                .happenedPlace(lostPost.getLocation())
                .upKindNm(lostPost.getUpKindNm().name())
                .kindNm(lostPost.getKindNm())
                .createdAt(lostPost.getCreatedAt())
                .feature(lostPost.getSpecialMark())
                .build();
    }
}

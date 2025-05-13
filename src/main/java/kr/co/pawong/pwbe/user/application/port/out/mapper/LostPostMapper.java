package kr.co.pawong.pwbe.user.application.port.out.mapper;

import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import kr.co.pawong.pwbe.user.application.port.out.dto.MyPageLostPostInfo;

public class LostPostMapper {

    public static MyPageLostPostInfo toMyPostLostPostInfo(LostPostCard lostPostCard) {

        return MyPageLostPostInfo.builder()
                .postId(lostPostCard.postId())
                .postType(lostPostCard.postType())
                .author(lostPostCard.author())
                .happenedDate(lostPostCard.happenedDate())
                .happenedPlace(lostPostCard.happenedPlace())
                .upKindNm(lostPostCard.upKindNm())
                .kindNm(lostPostCard.kindNm())
                .createdAt(lostPostCard.createdAt())
                .feature(lostPostCard.feature())
                .build();
    }
}

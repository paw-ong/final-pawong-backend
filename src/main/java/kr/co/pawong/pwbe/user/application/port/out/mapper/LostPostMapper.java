package kr.co.pawong.pwbe.user.application.port.out.mapper;

import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import kr.co.pawong.pwbe.user.application.port.out.dto.MyPageLostPostInfo;

public class LostPostMapper {

    /**
     * LostPost 도메인에서 받아온 LostPost DTO를 변환
     */
    public static MyPageLostPostInfo toMyPostLostPostInfo(LostPostCard lostPostCard) {

        return MyPageLostPostInfo.builder()
                .postId(lostPostCard.postId())
                .postType(lostPostCard.postType())
                .author(lostPostCard.author())
                .imageUrl(lostPostCard.imageUrl())
                .happenedDate(lostPostCard.happenedDate())
                .happenedPlace(lostPostCard.happenedPlace())
                .kindNm(lostPostCard.kindNm())
                .createdAt(lostPostCard.createdAt())
                .feature(lostPostCard.feature())
                .build();
    }
}

package kr.co.pawong.pwbe.user.application.port.in.mapper;

import kr.co.pawong.pwbe.global.util.TimeUtils;
import kr.co.pawong.pwbe.user.application.port.in.dto.MyPageLostPostResponse;
import kr.co.pawong.pwbe.user.application.port.out.dto.MyPageLostPostInfo;

public class MyPageMapper {

    public static MyPageLostPostResponse toMyPageLostPostResponse(
            MyPageLostPostInfo myPageLostPostInfo) {

        return MyPageLostPostResponse.builder()
                .postId(myPageLostPostInfo.postId())
                .postType(myPageLostPostInfo.postType())
                .author(myPageLostPostInfo.author())
                .happenedDate(
                        TimeUtils.toDateAgo(myPageLostPostInfo.happenedDate()))
                .happenedPlace(myPageLostPostInfo.happenedPlace())
                .upKindNm(myPageLostPostInfo.upKindNm())
                .kindNm(myPageLostPostInfo.kindNm())
                .createdAt(
                        TimeUtils.toTimeAgo(myPageLostPostInfo.createdAt()))
                .feature(myPageLostPostInfo.feature())
                .build();
    }

}

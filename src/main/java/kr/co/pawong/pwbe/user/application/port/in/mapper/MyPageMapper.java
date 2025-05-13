package kr.co.pawong.pwbe.user.application.port.in.mapper;

import kr.co.pawong.pwbe.global.time.TimeUtils;
import kr.co.pawong.pwbe.user.application.port.in.dto.MyPageLostPostResponse;
import kr.co.pawong.pwbe.user.application.port.out.dto.MyPageLostPostInfo;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MyPageMapper {

    private final TimeUtils timeUtils;

    public static MyPageLostPostResponse toMyPageLostPostResponse(
            MyPageLostPostInfo myPageLostPostInfo, TimeUtils timeUtils) {

        return MyPageLostPostResponse.builder()
                .postId(myPageLostPostInfo.postId())
                .postType(myPageLostPostInfo.postType())
                .author(myPageLostPostInfo.author())
                .happenedDate(
                        timeUtils.formatDateAgo(myPageLostPostInfo.happenedDate()))
                .happenedPlace(myPageLostPostInfo.happenedPlace())
                .upKindNm(myPageLostPostInfo.upKindNm())
                .kindNm(myPageLostPostInfo.kindNm())
                .createdAt(
                        timeUtils.formatTimeAgo(myPageLostPostInfo.createdAt()))
                .feature(myPageLostPostInfo.feature())
                .build();
    }

}

package kr.co.pawong.pwbe.user.application.port.in.mapper;

import kr.co.pawong.pwbe.user.application.port.in.dto.MyPageLostPostResponse;
import kr.co.pawong.pwbe.user.application.port.out.dto.MyPageLostPostInfo;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MyPageMapper {

    /**
     * 컨트롤러에게 전달할 응답 객체로 변환
     */
    public static MyPageLostPostResponse toMyPageLostPostResponse(
            MyPageLostPostInfo myPageLostPostInfo) {

        return MyPageLostPostResponse.builder()
                .postId(myPageLostPostInfo.postId())
                .postType(myPageLostPostInfo.postType())
                .author(myPageLostPostInfo.author())
                .happenedDate(myPageLostPostInfo.happenedDate())
                .happenedPlace(myPageLostPostInfo.happenedPlace())
                .upKindNm(myPageLostPostInfo.upKindNm())
                .kindNm(myPageLostPostInfo.kindNm())
                .createdAt(myPageLostPostInfo.createdAt())
                .feature(myPageLostPostInfo.feature())
                .build();
    }

}

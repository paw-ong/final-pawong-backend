package kr.co.pawong.pwbe.user.application.port.in.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import kr.co.pawong.pwbe.user.application.port.in.dto.MyPageLostPostResponse;
import kr.co.pawong.pwbe.user.application.port.out.dto.MyPageLostPostInfo;
import org.junit.jupiter.api.Test;

class MyPageMapperTest {

    @Test
    void 변환_함수_테스트() {
        // given
        MyPageLostPostInfo info = MyPageLostPostInfo.builder()
                .postId(99L)
                .postType("FOUND")
                .author("Bob")
                .happenedDate("2025-04-01")
                .happenedPlace("Busan")
                .upKindNm("CAT")
                .kindNm("Shorthair")
                .createdAt("2025-04-01 08:30:00")
                .feature("black and white")
                .build();

        // when
        MyPageLostPostResponse response = MyPageMapper.toMyPageLostPostResponse(info);

        // then
        assertThat(response.postId()).isEqualTo(99L);
        assertThat(response.postType()).isEqualTo("FOUND");
        assertThat(response.author()).isEqualTo("Bob");
        assertThat(response.happenedDate()).isEqualTo("2025-04-01");
        assertThat(response.happenedPlace()).isEqualTo("Busan");
        assertThat(response.upKindNm()).isEqualTo("CAT");
        assertThat(response.kindNm()).isEqualTo("Shorthair");
        assertThat(response.createdAt()).isEqualTo("2025-04-01 08:30:00");
        assertThat(response.feature()).isEqualTo("black and white");
    }
}
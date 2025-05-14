package kr.co.pawong.pwbe.user.application.port.out.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import kr.co.pawong.pwbe.user.application.port.out.dto.MyPageLostPostInfo;
import org.junit.jupiter.api.Test;

class LostPostMapperTest {
    @Test
    void 변환_함수_테스트() {
        // given
        LostPostCard card = LostPostCard.builder()
                .postId(42L)
                .postType("LOST")
                .author("Alice")
                .imageUrl("https://www.aaa.com")
                .happenedDate("2025-05-13")
                .happenedPlace("Seoul")
                .kindNm("Poodle")
                .createdAt("2025-05-13 12:12:02")
                .feature("white spot on tail")
                .build();

        // when
        MyPageLostPostInfo info = LostPostMapper.toMyPostLostPostInfo(card);

        // then
        assertThat(info.postId()).isEqualTo(42L);
        assertThat(info.postType()).isEqualTo("LOST");
        assertThat(info.author()).isEqualTo("Alice");
        assertThat(info.imageUrl()).isEqualTo("https://www.aaa.com");
        assertThat(info.happenedDate()).isEqualTo("2025-05-13");
        assertThat(info.happenedPlace()).isEqualTo("Seoul");
        assertThat(info.kindNm()).isEqualTo("Poodle");
        assertThat(info.createdAt()).isEqualTo("2025-05-13 12:12:02");
        assertThat(info.feature()).isEqualTo("white spot on tail");
    }
}
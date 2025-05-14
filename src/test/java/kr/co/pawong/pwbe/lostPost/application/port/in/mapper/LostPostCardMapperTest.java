package kr.co.pawong.pwbe.lostPost.application.port.in.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import kr.co.pawong.pwbe.adoption.enums.UpKindNm;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LostPostCardMapperTest {

    private Clock fixedClock;
    private static final LocalDateTime FIXED_LDT =
            LocalDateTime.of(2025, 5, 13, 12, 14, 3);
    private static final LocalDate FIXED_LD =
            LocalDate.of(2025, 5, 13);

    @BeforeEach
    void setUp() {
        fixedClock = Clock.fixed(FIXED_LDT.atZone(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault());
    }

    @Test
    void 변환_함수_테스트() {
        // given
        LocalDateTime createdAt = LocalDateTime.of(2025, 5, 13, 12, 12, 2);
        LostPost lostPost = LostPost.builder()
                .lostPostId(1L)
                .postType(PostType.LOST)
                .imageUrl("https://www.aaa.com")
                .date(LocalDate.of(2025, 4, 5))
                .location("Seoul")
                .upKindNm(UpKindNm.개)
                .kindNm("Poodle")
                .specialMark("white spot")
                .createdAt(createdAt)
                .build();

        // when
        LostPostCard card = LostPostCardMapper.toLostPostCard( lostPost, "Alice", fixedClock);

        // then
        assertThat(card.postId()).isEqualTo(1L);
        assertThat(card.postType()).isEqualTo("LOST");
        assertThat(card.author()).isEqualTo("Alice");
        assertThat(card.imageUrl()).isEqualTo("https://www.aaa.com");
        assertThat(card.happenedDate()).isEqualTo("2025-04-05");
        assertThat(card.happenedPlace()).isEqualTo("Seoul");
        assertThat(card.kindNm()).isEqualTo("Poodle");
        assertThat(card.createdAt()).isEqualTo("2분 전");
        assertThat(card.feature()).isEqualTo("white spot");
    }
}
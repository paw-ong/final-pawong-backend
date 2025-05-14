package kr.co.pawong.pwbe.lostPost.application.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import kr.co.pawong.pwbe.adoption.enums.UpKindNm;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostPostDataQueryPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.UserInfoPort;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QueryLostPostDataServiceTest {

    private QueryLostPostDataService service;
    private static final LocalDateTime FIXED_LDT =
            LocalDateTime.of(2025, 5, 13, 12, 14, 3);

    @BeforeEach
    void setUp() {
        // Fake 구현체들로 Service를 직접 생성
        LostPostDataQueryPort fakeLostPostPort = new FakeLostPostDataQueryPort();
        UserInfoPort fakeUserInfoPort = new FakeUserInfoPort();
        Clock fixedClock = Clock.fixed(FIXED_LDT.atZone(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault());

        service = new QueryLostPostDataService(
                fakeLostPostPort,
                fakeUserInfoPort,
                fixedClock
        );
    }

    @Test
    void UserId_로_LostPostCard_조회하기() {
        // when
        List<LostPostCard> cards = service.getLostPostsByUserId(123L);

        // then
        assertThat(cards).hasSize(1);
        LostPostCard card = cards.get(0);

        // FakeLostPost 제공값 기준으로 검증
        assertThat(card.postId()).isEqualTo(555L);
        assertThat(card.postType()).isEqualTo("FOUND");
        assertThat(card.author()).isEqualTo("fake-nick");
        assertThat(card.happenedDate()).isEqualTo("2025-05-01");
        assertThat(card.happenedPlace()).isEqualTo("Seoul");
        assertThat(card.kindNm()).isEqualTo("푸들");
        assertThat(card.createdAt()).isEqualTo("12일 전");    // FakeTimeUtils 기준
        assertThat(card.feature()).isEqualTo("흰 점이 있음");
    }

    @Test
    void UserId_로_LostPostCard_조회하는데_해당하는_LostPost_가_없는_경우_빈_리스트_반환() {
        // FakeLostPostPort는 userId!=123L일 때 빈 리스트를 반환
        List<LostPostCard> empty = service.getLostPostsByUserId(999L);
        assertThat(empty).isEmpty();
    }

    // --- 테스트를 위한 페이크 구현체 ---

    /** userId == 123L 일 때만 하나의 LostPost를, 아니면 빈 리스트를 반환 */
    static class FakeLostPostDataQueryPort implements LostPostDataQueryPort {
        @Override
        public List<LostPost> getLostPostsByUserId(Long userId) {
            if (userId.equals(123L)) {
                LostPost lp = LostPost.builder()
                        .lostPostId(555L)
                        .postType(PostType.FOUND)
                        .date(LocalDate.of(2025, 5, 1))
                        .location("Seoul")
                        .upKindNm(UpKindNm.개)
                        .kindNm("푸들")
                        .specialMark("흰 점이 있음")
                        .createdAt(LocalDateTime.of(2025,5,1,10,0,0))
                        .build();
                return List.of(lp);
            }
            return List.of();
        }

        @Override
        public LostPost findLostPostById(Long lostPostId) {
            if (lostPostId.equals(123L)) {
                LostPost lp = LostPost.builder()
                        .lostPostId(555L)
                        .postType(PostType.FOUND)
                        .date(LocalDate.of(2025, 5, 1))
                        .location("Seoul")
                        .upKindNm(UpKindNm.개)
                        .kindNm("푸들")
                        .specialMark("흰 점이 있음")
                        .createdAt(LocalDateTime.of(2025,5,1,10,0,0))
                        .build();
                return lp;
            }
            return null;
        }
    }

    /** userId == 123L 일 때만 고정 닉네임을 반환 */
    static class FakeUserInfoPort implements UserInfoPort {
        @Override
        public String getNicknameByUserId(Long userId) {
            return userId.equals(123L) ? "fake-nick" : "";
        }
    }

}
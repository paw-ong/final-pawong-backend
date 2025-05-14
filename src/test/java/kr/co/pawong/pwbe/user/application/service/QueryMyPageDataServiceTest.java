package kr.co.pawong.pwbe.user.application.service;



import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.List;
import kr.co.pawong.pwbe.user.application.port.in.QueryBookmarkDataUseCase;
import kr.co.pawong.pwbe.user.application.port.in.dto.MyPageLostPostResponse;
import kr.co.pawong.pwbe.user.application.port.out.LostPostInfoPort;
import kr.co.pawong.pwbe.user.application.port.out.dto.MyPageLostPostInfo;
import kr.co.pawong.pwbe.user.domain.LostBookmark;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QueryMyPageDataServiceTest {

    private QueryMyPageDataService service;

    @BeforeEach
    void setUp() {
        LostPostInfoPort fakePort = new FakeLostPostInfoPort();
        // TODO: FAKE_로 만들어서 넣어줘야함.
        QueryBookmarkDataUseCase queryBookmarkDataUseCase = null;
        service = new QueryMyPageDataService(fakePort, queryBookmarkDataUseCase);
    }

    @Test
    void UserId_로_LostPost_조회하기() {
        // when
        List<MyPageLostPostResponse> responses = service.getLostPostsByUserId(123L);

        // then
        assertThat(responses).hasSize(1);
        MyPageLostPostResponse res = responses.get(0);
        assertThat(res.postId()).isEqualTo(55L);
        assertThat(res.postType()).isEqualTo("FOUND");
        assertThat(res.author()).isEqualTo("fake-author");
        assertThat(res.happenedDate()).isEqualTo("2025-05-01");
        assertThat(res.happenedPlace()).isEqualTo("FakeCity");
        assertThat(res.upKindNm()).isEqualTo("고양이");
        assertThat(res.kindNm()).isEqualTo("Tabby");
        assertThat(res.createdAt()).isEqualTo("2025-05-01 09:00:00");
        assertThat(res.feature()).isEqualTo("none");
    }

    @Test
    void UserId_로_LostPost_조회할_때_해당하는_LostPost_가_없는_경우_빈_리스트_반환() {
        List<MyPageLostPostResponse> responses = service.getLostPostsByUserId(999L);
        assertThat(responses).isEmpty();
    }

    // --- 테스트를 위한 페이크 구현체 ---
    static class FakeLostPostInfoPort implements LostPostInfoPort {
        @Override
        public List<MyPageLostPostInfo> getLostPostsByUserId(Long userId) {
            if (!userId.equals(123L)) {
                return List.of();
            }
            MyPageLostPostInfo info = MyPageLostPostInfo.builder()
                    .postId(55L)
                    .postType("FOUND")
                    .author("fake-author")
                    .happenedDate("2025-05-01")
                    .happenedPlace("FakeCity")
                    .upKindNm("고양이")
                    .kindNm("Tabby")
                    .createdAt("2025-05-01 09:00:00")
                    .feature("none")
                    .build();
            return List.of(info);
        }

        // TODO: 함수 오버라이딩 작성
        @Override
        public List<MyPageLostPostInfo> getLostAnimalsByLostPostIds(
                List<LostBookmark> lostPostIds) {
            return List.of();
        }
    }
}
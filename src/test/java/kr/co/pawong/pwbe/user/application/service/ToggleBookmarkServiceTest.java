package kr.co.pawong.pwbe.user.application.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import kr.co.pawong.pwbe.user.application.port.out.LostBookmarkCommandPort;
import kr.co.pawong.pwbe.user.application.port.out.LostBookmarkQueryPort;
import kr.co.pawong.pwbe.user.domain.LostBookmark;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ToggleBookmarkServiceTest {

    private ToggleBookmarkService service;
    private FakeBookmarkPort fakePort;

    @BeforeEach
    void setUp() {
        fakePort = new FakeBookmarkPort();
        service = new ToggleBookmarkService(fakePort, fakePort);
    }

    @Test
    void LostPostId_로_LostBookmark_토글_테스트() {
        long userId = 1, lostPostId = 10;

        // 1) 없을 때 → 생성(true 반환)
        boolean first = service.toggleLostPostBookmark(userId, lostPostId);
        assertThat(first).isTrue();
        assertThat(fakePort.count()).isEqualTo(1);

        // 2) 있을 때 → 삭제(false 반환)
        boolean second = service.toggleLostPostBookmark(userId, lostPostId);
        assertThat(second).isFalse();
        assertThat(fakePort.count()).isZero();
    }

    @Test
    void AdoptionId_로_LostBookmark_토글_테스트() {
        long userId = 2, adoptionId = 20;

        // 1) 없을 때 → 생성(true 반환)
        boolean first = service.toggleLostAdoptionBookmark(userId, adoptionId);
        assertThat(first).isTrue();
        assertThat(fakePort.count()).isEqualTo(1);

        // 2) 있을 때 → 삭제(false 반환)
        boolean second = service.toggleLostAdoptionBookmark(userId, adoptionId);
        assertThat(second).isFalse();
        assertThat(fakePort.count()).isZero();
    }

    /**
     * 테스트 의존성 주입을 위한 페이크 구현체
     */
    static class FakeBookmarkPort implements LostBookmarkQueryPort, LostBookmarkCommandPort {

        private final Map<Long, LostBookmark> store = new HashMap<>();
        private long seq = 1L;

        @Override
        public Optional<LostBookmark> findByLostPostId(long userId, long lostPostId) {
            return store.values().stream()
                    .filter(b -> b.getUserId() == userId
                            && Objects.equals(b.getLostPostId(), lostPostId))
                    .findFirst();
        }

        @Override
        public Optional<LostBookmark> findByAdoptionId(long userId, long adoptionId) {
            return store.values().stream()
                    .filter(b -> b.getUserId() == userId
                            && Objects.equals(b.getAdoptionId(), adoptionId))
                    .findFirst();
        }

        // TODO: 함수 오버라이딩 작성
        @Override
        public List<LostBookmark> findByUserId(long userId) {
            return List.of();
        }

        @Override
        public boolean lostPostBookmarkExists(Long userId, long lostPostId) {
            return false;
        }

        @Override
        public boolean lostAdoptionBookmarkExists(Long userId, long bookmarkId) {
            return false;
        }

        @Override
        public LostBookmark save(LostBookmark bookmark) {
            LostBookmark newBookmark = LostBookmark.builder()
                    .bookmarkId(seq++)
                    .userId(bookmark.getUserId())
                    .adoptionId(bookmark.getAdoptionId())
                    .lostPostId(bookmark.getLostPostId())
                    .build();

            store.put(newBookmark.getBookmarkId(), newBookmark);
            return newBookmark;
        }

        @Override
        public void delete(Long bookmarkId) {
            store.remove(bookmarkId);
        }

        @Override
        public void deleteByLostPostId(Long lostPostId) {
            store.remove(lostPostId);
        }

        int count() {
            return store.size();
        }
    }
}
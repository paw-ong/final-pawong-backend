package kr.co.pawong.pwbe.user.domain;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.jupiter.api.Test;

class LostBookmarkTest {

    @Test
    void LostPostId_로_LostBookmark_생성_검증() {
        // given
        long userId = 2;
        long lostPostId = 12;

        // when
        LostBookmark bookmark = LostBookmark.createByLostPostId(userId, lostPostId);

        // then
        assertThat(bookmark.getUserId()).isEqualTo(userId);
        assertThat(bookmark.getLostPostId()).isEqualTo(lostPostId);
        // 제약 조건 검증
        assertThat(bookmark.getBookmarkId()).isNull();
        assertThat(bookmark.getAdoptionId()).isNull();
    }

    @Test
    void AdoptionId_로_LostBookmark_생성_검증() {
        // given
        long userId = 2;
        long adoptionId = 12;

        // when
        LostBookmark bookmark = LostBookmark.createByAdoptionId(userId, adoptionId);

        // then
        assertThat(bookmark.getUserId()).isEqualTo(userId);
        assertThat(bookmark.getAdoptionId()).isEqualTo(adoptionId);
        // 제약 조건 검증
        assertThat(bookmark.getBookmarkId()).isNull();
        assertThat(bookmark.getLostPostId()).isNull();
    }
  
}
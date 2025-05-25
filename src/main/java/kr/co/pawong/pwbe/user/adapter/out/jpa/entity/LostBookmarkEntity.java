package kr.co.pawong.pwbe.user.adapter.out.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.co.pawong.pwbe.user.domain.LostBookmark;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "LostBookmarks")
// TODO: 제약조건 추가해야 됨 (외래키 제약조건 + lostPostId와 adoptionId 제약 조건)
// TODO: 그리고 없는 userId나 lostPostId, adoptionId에 대해 데이터 insert시에 발생하는 에러 예외처리 해야 됨
public class LostBookmarkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmarkId;

    private Long userId;

    private Long lostPostId;
    private Long adoptionId;

    public static LostBookmarkEntity of(LostBookmark lostBookmark) {

        return LostBookmarkEntity.builder()
                .bookmarkId(lostBookmark.getBookmarkId())
                .userId(lostBookmark.getUserId())
                .lostPostId(lostBookmark.getLostPostId())
                .adoptionId(lostBookmark.getAdoptionId())
                .build();
    }

    public LostBookmark toDomain() {
        return LostBookmark.builder()
                .bookmarkId(this.bookmarkId)
                .userId(this.userId)
                .lostPostId(this.lostPostId)
                .adoptionId(this.adoptionId)
                .build();
    }
}

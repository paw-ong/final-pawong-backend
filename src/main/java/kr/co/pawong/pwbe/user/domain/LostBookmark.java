package kr.co.pawong.pwbe.user.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LostBookmark {

    private Long bookmarkId;
    private Long userId;
    private Long lostPostId;
    private Long adoptionId;

    public static LostBookmark createByLostPostId(long userId, long lostPostId) {
        return LostBookmark.builder()
                .userId(userId)
                .lostPostId(lostPostId)
                .build();
    }

    public static LostBookmark createByAdoptionId(long userId, long adoptionId) {
        return LostBookmark.builder()
                .userId(userId)
                .adoptionId(adoptionId)
                .build();
    }
}

package kr.co.pawong.pwbe.user.domain;

import kr.co.pawong.pwbe.user.adapter.out.jpa.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LostBookmark {

    private Long bookmarkId;
    private Long userId;
    private Long lostPostId;
    private Long adoptionId;
}

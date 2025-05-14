package kr.co.pawong.pwbe.user.adapter.out.lostPost.mapper;

import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAnimalQuery;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAnimalQuery.LostType;
import kr.co.pawong.pwbe.user.domain.LostBookmark;

public class LostAnimalQueryMapper {

    public static LostAnimalQuery toLostAnimalQuery(LostBookmark lostBookmark) {

        // LostPostId_가 있으면 LostPost_에 대한 즐겨찾기
        if (lostBookmark.getLostPostId() != null) {
            return LostAnimalQuery.builder()
                    .type(LostType.LOST_POST)
                    .id(lostBookmark.getLostPostId())
                    .build();
        } else {
            return LostAnimalQuery.builder()
                    .type(LostType.LOST_ADOPTION)
                    .id(lostBookmark.getAdoptionId())
                    .build();
        }
    }

}

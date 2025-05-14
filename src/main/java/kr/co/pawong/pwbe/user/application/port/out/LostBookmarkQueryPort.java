package kr.co.pawong.pwbe.user.application.port.out;

import java.util.Optional;
import kr.co.pawong.pwbe.user.domain.LostBookmark;

public interface LostBookmarkQueryPort {

    Optional<LostBookmark> findLostBookmarkByLostPostId(long userId, long lostPostId);

    Optional<LostBookmark> findLostBookmarkByAdoptionId(long userId, long adoptionId);
}

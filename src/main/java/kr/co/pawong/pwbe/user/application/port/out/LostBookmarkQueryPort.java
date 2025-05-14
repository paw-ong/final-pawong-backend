package kr.co.pawong.pwbe.user.application.port.out;

import java.util.Optional;
import kr.co.pawong.pwbe.user.domain.LostBookmark;

public interface LostBookmarkQueryPort {

    Optional<LostBookmark> findByLostPostId(long userId, long lostPostId);

    Optional<LostBookmark> findByAdoptionId(long userId, long adoptionId);
}

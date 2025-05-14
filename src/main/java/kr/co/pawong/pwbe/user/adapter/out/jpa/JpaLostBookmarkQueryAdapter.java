package kr.co.pawong.pwbe.user.adapter.out.jpa;

import java.util.Optional;
import kr.co.pawong.pwbe.user.adapter.out.jpa.entity.LostBookmarkEntity;
import kr.co.pawong.pwbe.user.adapter.out.jpa.repository.LostBookmarkRepository;
import kr.co.pawong.pwbe.user.application.port.out.LostBookmarkQueryPort;
import kr.co.pawong.pwbe.user.domain.LostBookmark;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaLostBookmarkQueryAdapter implements LostBookmarkQueryPort {

    private final LostBookmarkRepository lostBookmarkRepository;

    @Override
    public Optional<LostBookmark> findByLostPostId(long userId, long lostPostId) {
        return lostBookmarkRepository.findLostBookmarkEntityByUserIdAndLostPostId(userId, lostPostId)
                .map(LostBookmarkEntity::toDomain);
    }

    @Override
    public Optional<LostBookmark> findByAdoptionId(long userId, long adoptionId) {
        return lostBookmarkRepository.findLostBookmarkEntityByUserIdAndAdoptionId(userId, adoptionId)
                .map(LostBookmarkEntity::toDomain);
    }
}

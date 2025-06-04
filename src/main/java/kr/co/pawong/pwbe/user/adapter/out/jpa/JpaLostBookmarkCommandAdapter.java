package kr.co.pawong.pwbe.user.adapter.out.jpa;

import kr.co.pawong.pwbe.user.adapter.out.jpa.entity.LostBookmarkEntity;
import kr.co.pawong.pwbe.user.adapter.out.jpa.repository.LostBookmarkRepository;
import kr.co.pawong.pwbe.user.application.port.out.LostBookmarkCommandPort;
import kr.co.pawong.pwbe.user.domain.LostBookmark;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaLostBookmarkCommandAdapter implements LostBookmarkCommandPort {

    private final LostBookmarkRepository lostBookmarkRepository;

    @Override
    public LostBookmark save(LostBookmark bookmark) {
        // 북마크는 업데이트 하는 경우가 없어서 save_로만 저장합니다.
        return lostBookmarkRepository.save(LostBookmarkEntity.of(bookmark))
                .toDomain();
    }

    @Override
    public void delete(Long bookmarkId) {
        lostBookmarkRepository.deleteById(bookmarkId);
    }

    @Override
    public void deleteByLostPostId(Long lostPostId) {
        lostBookmarkRepository.deleteByLostPostId(lostPostId);
    }
}

package kr.co.pawong.pwbe.user.application.service;

import java.util.List;
import kr.co.pawong.pwbe.user.application.port.in.QueryBookmarkDataUseCase;
import kr.co.pawong.pwbe.user.application.port.out.LostBookmarkQueryPort;
import kr.co.pawong.pwbe.user.domain.LostBookmark;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QueryBookmarkDataService implements QueryBookmarkDataUseCase {

    private final LostBookmarkQueryPort lostBookmarkQueryPort;

    @Override
    @Transactional(readOnly = true)
    public List<LostBookmark> getBookmarksByUserId(long userId) {
        return lostBookmarkQueryPort.findByUserId(userId);
    }

    @Override
    public boolean existsLostPostBookmark(Long userId, long lostPostId) {
        return lostBookmarkQueryPort.lostPostBookmarkExists(userId, lostPostId);
    }

    @Override
    public boolean existsLostAdoptionBookmark(Long userId, long adoptionId) {
        return lostBookmarkQueryPort.lostAdoptionBookmarkExists(userId, adoptionId);
    }
}

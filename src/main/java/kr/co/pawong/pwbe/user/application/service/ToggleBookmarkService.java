package kr.co.pawong.pwbe.user.application.service;

import kr.co.pawong.pwbe.user.application.port.in.ToggleBookmarkUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ToggleBookmarkService implements ToggleBookmarkUseCase {

    @Override
    public boolean toggleLostPostBookmark(Long userId, Long lostPostId) {
        return false;
    }
}

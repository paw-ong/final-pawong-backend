package kr.co.pawong.pwbe.lostPost.application.service;

import kr.co.pawong.pwbe.lostPost.application.port.in.UpdateLostPostUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostPostUpdatePort;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LostPostUpdateService implements UpdateLostPostUseCase {

    private final LostPostUpdatePort lostPostUpdatePort;

    @Override
    public Long createLostPost(LostPost lostPost, Long userId) {
        lostPost.writtenBy(userId);
        lostPost.create();
        return lostPostUpdatePort.saveLostPost(lostPost).getLostPostId();
    }
}

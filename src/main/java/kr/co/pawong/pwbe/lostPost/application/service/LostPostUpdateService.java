package kr.co.pawong.pwbe.lostPost.application.service;

import kr.co.pawong.pwbe.lostPost.application.port.in.LostPostUpdateUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostPostUpdatePort;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LostPostUpdateService implements LostPostUpdateUseCase {

    private final LostPostUpdatePort lostPostUpdatePort;

    @Override
    public LostPost createLostPost(LostPost lostPost, Long userId) {
        lostPost.writedBy(userId);
        lostPost.create();
        return lostPostUpdatePort.saveLostPost(lostPost);
    }
}

package kr.co.pawong.pwbe.lostPost.application.service;

import kr.co.pawong.pwbe.lostPost.application.port.in.CommandLostPostDataUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.out.LostPostDataCommandPort;
import kr.co.pawong.pwbe.lostPost.domain.LostPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommandLostPostDataService implements CommandLostPostDataUseCase {

    private final LostPostDataCommandPort lostPostUpdatePort;

    @Override
    public Long createLostPost(LostPost lostPost, Long userId) {
        lostPost.writtenBy(userId);
        lostPost.create();
        return lostPostUpdatePort.saveLostPost(lostPost).getLostPostId();
    }
}

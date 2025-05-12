package kr.co.pawong.pwbe.lostPost.application.port.in;

import kr.co.pawong.pwbe.lostPost.domain.LostPost;

public interface LostPostUpdateUseCase {
    LostPost createLostPost(LostPost lostPost, Long userId);
}

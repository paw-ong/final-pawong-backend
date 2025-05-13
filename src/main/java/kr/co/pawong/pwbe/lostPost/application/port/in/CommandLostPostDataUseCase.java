package kr.co.pawong.pwbe.lostPost.application.port.in;

import kr.co.pawong.pwbe.lostPost.domain.LostPost;

public interface CommandLostPostDataUseCase {

    Long createLostPost(LostPost lostPost, Long userId);
}

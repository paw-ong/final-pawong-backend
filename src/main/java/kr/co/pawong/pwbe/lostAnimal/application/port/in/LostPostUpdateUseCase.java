package kr.co.pawong.pwbe.lostAnimal.application.port.in;

import kr.co.pawong.pwbe.lostAnimal.domain.LostPost;

public interface LostPostUpdateUseCase {
    LostPost createLostPost(LostPost lostPost, Long userId);
}

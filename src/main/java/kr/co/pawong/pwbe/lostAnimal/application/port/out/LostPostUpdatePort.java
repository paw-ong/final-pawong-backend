package kr.co.pawong.pwbe.lostAnimal.application.port.out;

import kr.co.pawong.pwbe.lostAnimal.domain.LostPost;

public interface LostPostUpdatePort {
    LostPost saveLostPost(LostPost LostPost);
}

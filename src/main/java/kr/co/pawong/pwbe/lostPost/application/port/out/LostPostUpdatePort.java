package kr.co.pawong.pwbe.lostPost.application.port.out;

import kr.co.pawong.pwbe.lostPost.domain.LostPost;

public interface LostPostUpdatePort {
    LostPost saveLostPost(LostPost LostPost);
}

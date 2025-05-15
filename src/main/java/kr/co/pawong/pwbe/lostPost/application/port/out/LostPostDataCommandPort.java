package kr.co.pawong.pwbe.lostPost.application.port.out;

import kr.co.pawong.pwbe.lostPost.domain.LostPost;

public interface LostPostDataCommandPort {

    LostPost saveLostPost(LostPost LostPost);

    void updateDeleteStatus(Long postId, Long userId);

}

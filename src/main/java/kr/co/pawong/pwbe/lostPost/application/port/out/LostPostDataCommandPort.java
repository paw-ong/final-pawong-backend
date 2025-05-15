package kr.co.pawong.pwbe.lostPost.application.port.out;

import kr.co.pawong.pwbe.lostPost.domain.LostPost;

public interface LostPostDataCommandPort {

    LostPost saveLostPost(LostPost LostPost);

    LostPost updateLostPostOrThrow(Long postId, LostPost lostPost, Long userId);

    void modifyDeleteStatusOrThrow(Long postId, Long userId);

}

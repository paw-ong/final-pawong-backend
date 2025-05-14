package kr.co.pawong.pwbe.lostPost.application.port.out;

import kr.co.pawong.pwbe.lostPost.domain.LostPost;

public interface LostPostDataQueryPort {

    LostPost findLostPostById(Long lostPostId);

}

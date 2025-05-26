package kr.co.pawong.pwbe.lostPost.application.service;

import kr.co.pawong.pwbe.lostPost.application.port.in.NotifyUsersOfSimilarLostPostsUseCase;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotifyUsersOfSimilarLostPostsService implements NotifyUsersOfSimilarLostPostsUseCase {

    @Override
    public void notifyUsersOfSimilarLostPosts(long id, PostType type, float[] embedding) {

    }
}

package kr.co.pawong.pwbe.lostPost.application.service;

import kr.co.pawong.pwbe.lostPost.application.port.in.NotifyUsersOfSimilarLostPostsUseCase;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotifyUsersOfSimilarLostPostsService implements NotifyUsersOfSimilarLostPostsUseCase {

    /**
     * 1. 데이터 ES_에 인덱싱
     * 2. 현재 게시글과 유사한 LOST 타입 문서를 ES_에서 검색
     * 3. 해당 실종 게시글 작성자에게 알림 
     */
    @Override
    public void notifyUsersOfSimilarLostPosts(long id, PostType type, float[] embedding) {
        // 1. 데이터 ES_에 인덱싱
        
        // 2. ES 검색
        
        // 3. 알림 호출

    }
}

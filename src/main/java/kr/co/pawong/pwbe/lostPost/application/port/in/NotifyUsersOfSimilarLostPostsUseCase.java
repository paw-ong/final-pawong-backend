package kr.co.pawong.pwbe.lostPost.application.port.in;

import kr.co.pawong.pwbe.lostPost.enums.PostType;

public interface NotifyUsersOfSimilarLostPostsUseCase {

    /**
     * 등록된 "발견" 신고 또는 추가된 "구조" 동물과 유사한 실종 신고를 한 유저에게 알림을 보내는 UseCase_입니다.
     * @param id - FOUND 게시물 또는 RESCUED(FOSTER) 게시물 id_입니다.
     * @param type - FOUND 또는 RESCUED
     * @param embedding - 임베딩 벡터
     */
    void notifyUsersOfSimilarLostPosts(long id, PostType type, float[] embedding);
}

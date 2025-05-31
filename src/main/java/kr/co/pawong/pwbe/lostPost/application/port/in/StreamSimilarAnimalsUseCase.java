package kr.co.pawong.pwbe.lostPost.application.port.in;

import kr.co.pawong.pwbe.lostPost.enums.PostType;

public interface StreamSimilarAnimalsUseCase {

    /**
     * 실종 신고 게시물이 등록되고 임베딩이 끝난 후에
     * 유사한 실종 동물을 찾아서 클라이언트에게 보내주는 UseCase_입니다.
     * @param id - LOST 게시물 id_입니다.
     * @param type - LOST
     * @param embedding - 임베딩 벡터
     */
    void streamSimilarAnimals(long id, PostType type, float[] embedding);
}

package kr.co.pawong.pwbe.lostPost.application.port.in;

import kr.co.pawong.pwbe.lostPost.enums.PostType;

public interface IndexLostAnimalUseCase {

    void index(long id, PostType type, float[] embedding);
}

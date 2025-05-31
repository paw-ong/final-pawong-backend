package kr.co.pawong.pwbe.lostPost.application.port.in;

import kr.co.pawong.pwbe.lostPost.enums.PostType;

public interface PublishCreatedLostAnimalUseCase {

    void publishCreatedLostAnimal(
            long id,
            PostType type,
            String queryText,
            String queryImageUrl
    );
}

package kr.co.pawong.pwbe.lostPost.application.port.out;

import kr.co.pawong.pwbe.lostPost.application.port.out.dto.CreatedLostAnimalPublishDto;

public interface LostAnimalMessagePublishPort {

    void publishLostPostCreatedMessage(CreatedLostAnimalPublishDto message);

    void publishFoundPostCreatedMessage(CreatedLostAnimalPublishDto message);
}

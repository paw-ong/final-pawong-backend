package kr.co.pawong.pwbe.lostPost.application.port.out;

import kr.co.pawong.pwbe.lostPost.application.port.out.dto.CreatedLostPostPublishDto;

public interface LostPostMessagePublishPort {

    void publishLostPostCreatedMessage(CreatedLostPostPublishDto message);
}

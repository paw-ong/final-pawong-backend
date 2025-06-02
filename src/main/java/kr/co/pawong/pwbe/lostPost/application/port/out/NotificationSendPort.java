package kr.co.pawong.pwbe.lostPost.application.port.out;

import kr.co.pawong.pwbe.lostPost.enums.PostType;

public interface NotificationSendPort {

    void sendNotification(Long userId, Long targetId, PostType postType);
}

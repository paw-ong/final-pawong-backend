package kr.co.pawong.pwbe.chat.application.port.out;

import kr.co.pawong.pwbe.user.domain.User;

public interface ChatMessageBrokerPort {
    void sendMessageToUser(String username, String destination, Object object);
    boolean isUserSubscribedToRoom(User receiver, Long roomId);

}

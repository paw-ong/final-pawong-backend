package kr.co.pawong.pwbe.chat.application.port.out;

public interface ChatMessageBrokerPort {
    void sendMessageToUser(String username, String destination, Object object);
}

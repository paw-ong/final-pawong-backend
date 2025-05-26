package kr.co.pawong.pwbe.chat.adapter.out.broker;

import kr.co.pawong.pwbe.chat.application.port.out.ChatMessageBrokerPort;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SimpleBrokerChatMessageAdapter implements ChatMessageBrokerPort {

    private final SimpMessagingTemplate template;

    @Override
    public void sendMessageToUser(String username, String destination, Object object) {
        template.convertAndSendToUser(username, destination, object);

    }
}

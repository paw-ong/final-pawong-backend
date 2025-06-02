package kr.co.pawong.pwbe.chat.adapter.out.broker;

import kr.co.pawong.pwbe.chat.application.port.out.ChatMessageBrokerPort;
import kr.co.pawong.pwbe.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SimpleBrokerChatMessageAdapter implements ChatMessageBrokerPort {

    private final SimpMessagingTemplate template;
    private final SimpUserRegistry simpUserRegistry;

    @Override
    public void sendMessageToUser(String username, String destination, Object object) {
        template.convertAndSendToUser(username, destination, object);
    }

    @Override
    public boolean isUserSubscribedToRoom(User receiver, Long roomId) {
        Long socialId = receiver.getSocialId();
        SimpUser simpUser = simpUserRegistry.getUser(String.valueOf(socialId));
        if (simpUser == null) {
            return false;
        }
        String expectDestination = "/user/queue/chat/" + roomId;
        return simpUser.getSessions()
                .stream()
                .flatMap(session -> session.getSubscriptions().stream())
                .anyMatch(sub -> expectDestination.equals(sub.getDestination()));

    }
}

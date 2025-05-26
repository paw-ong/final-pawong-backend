package kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa;

import java.util.Objects;
import kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa.entity.ChatMessageEntity;
import kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa.repository.ChatMessageJpaRepository;
import kr.co.pawong.pwbe.chat.application.port.out.ChatMessageDataCommandPort;
import kr.co.pawong.pwbe.chat.domain.ChatMessage;
import kr.co.pawong.pwbe.chat.enums.ChatMessageStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaChatMessageDataCommandAdapter implements ChatMessageDataCommandPort {

    private final ChatMessageJpaRepository chatMessageJpaRepository;

    @Override
    public ChatMessage saveChatMessage(ChatMessage chatMessage) {
        ChatMessageEntity savedMessage = chatMessageJpaRepository.save(
                ChatMessageEntity.from(chatMessage));
        return savedMessage.toModel();
    }

    @Override
    public void readChatMessage(Long charRoomId, Long userId) {
        chatMessageJpaRepository.findChatMessagesByChatRoomIdOrderByCreatedAtAsc(charRoomId).stream()
                .filter(msg -> isUnreadByOther(msg, userId))
                .forEach(ChatMessageEntity::readMessage);
    }

    /**
     * 해당 메시지가 '다른 사람이 보낸' & '아직 읽지 않은(SENT)' 메시지인지 판별
     * @param msg
     * @param userId
     * @return
     */
    private boolean isUnreadByOther(ChatMessageEntity msg, Long userId) {
        return !Objects.equals(msg.getSenderId(), userId)
                && msg.getStatus() == ChatMessageStatus.SENT;
    }
}

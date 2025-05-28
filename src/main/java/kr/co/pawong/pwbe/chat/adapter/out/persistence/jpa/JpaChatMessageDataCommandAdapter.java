package kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa;

import kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa.entity.ChatMessageEntity;
import kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa.repository.ChatMessageJpaRepository;
import kr.co.pawong.pwbe.chat.application.port.out.ChatMessageDataCommandPort;
import kr.co.pawong.pwbe.chat.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaChatMessageDataCommandAdapter implements ChatMessageDataCommandPort {

    private final ChatMessageJpaRepository chatMessageJpaRepository;

    @Override
    public Long saveChatMessage(ChatMessage chatMessage) {
        ChatMessageEntity savedMessage = chatMessageJpaRepository.save(
                ChatMessageEntity.from(chatMessage));
        return savedMessage.getMessageId();
    }
}

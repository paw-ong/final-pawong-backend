package kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa;

import java.util.List;
import java.util.stream.Collectors;
import kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa.entity.ChatMessageEntity;
import kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa.repository.ChatMessageJpaRepository;
import kr.co.pawong.pwbe.chat.application.port.out.ChatMessageDataQueryPort;
import kr.co.pawong.pwbe.chat.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaChatMessageDataQueryAdapter implements ChatMessageDataQueryPort {

    private final ChatMessageJpaRepository chatMessageJpaRepository;

    // 채팅방에 속한 모든 메시지들을 최근 생성 순으로 갖고오기
    @Override
    public List<ChatMessage> findChatMessagesByChatRoomIdInLatestOrder(Long chatRoomId) {
        return chatMessageJpaRepository
                .findChatMessagesByChatRoomIdOrderByCreatedAtDesc(chatRoomId)
                .stream()
                .map(ChatMessageEntity::toModel)
                .collect(Collectors.toList());
    }
}

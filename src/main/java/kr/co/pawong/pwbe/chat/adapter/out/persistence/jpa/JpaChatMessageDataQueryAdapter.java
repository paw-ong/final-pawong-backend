package kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.*;

import java.util.List;
import java.util.stream.Collectors;
import kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa.entity.ChatMessageEntity;
import kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa.repository.ChatMessageJpaRepository;
import kr.co.pawong.pwbe.chat.application.port.out.ChatMessageDataQueryPort;
import kr.co.pawong.pwbe.chat.domain.ChatMessage;
import kr.co.pawong.pwbe.chat.enums.ChatMessageStatus;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaChatMessageDataQueryAdapter implements ChatMessageDataQueryPort {

    private final ChatMessageJpaRepository chatMessageJpaRepository;

    // 채팅방에 속한 모든 메시지들을 최근 생성 순으로 갖고오기
    @Override
    public List<ChatMessage> findChatMessagesByChatRoomIdInLatestOrder(Long chatRoomId) {
        return chatMessageJpaRepository
                .findChatMessagesByChatRoomIdOrderByCreatedAtAsc(chatRoomId)
                .stream()
                .map(ChatMessageEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public ChatMessage findLatestReadMessageOrThrow(Long chatRoomId, Long userId) {
        return chatMessageJpaRepository
                .findFirstByChatRoomIdAndStatusAndSenderIdNotOrderByCreatedAtDesc(chatRoomId, ChatMessageStatus.READ, userId)
                .orElseThrow(() -> new BaseException(CHATMESSAGE_NOT_FOUND))
                .toModel();
    }
}

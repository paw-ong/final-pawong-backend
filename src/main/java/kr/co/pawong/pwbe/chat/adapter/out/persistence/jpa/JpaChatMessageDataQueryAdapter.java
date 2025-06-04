package kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.*;

import java.util.List;
import java.util.stream.Collectors;
import kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa.entity.ChatMessageEntity;
import kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa.repository.ChatMessageJpaRepository;
import kr.co.pawong.pwbe.chat.application.port.out.ChatMessageDataQueryPort;
import kr.co.pawong.pwbe.chat.domain.ChatMessage;
import kr.co.pawong.pwbe.chat.enums.ChatMessageStatus;
import kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

    // 채팅방 내에 가장 마지막으로 생성된 메시지 조회
    @Override
    public ChatMessage findLatestChatMessageInChatRoomOrThrow(Long chatRoomId) {
        ChatMessageEntity chatMessageEntity = chatMessageJpaRepository.findFirstByChatRoomIdOrderByCreatedAtDesc(
                        chatRoomId)
                .orElseThrow(() -> new BaseException(CHATMESSAGE_NOT_FOUND));
        return chatMessageEntity.toModel();
    }

    // 내가 가장 최근에 읽은 메세지 가져오기
    @Override
    public ChatMessage findLatestReadMessageOrThrow(Long chatRoomId, Long userId) {
        return chatMessageJpaRepository
                .findFirstByChatRoomIdAndStatusAndSenderIdNotOrderByCreatedAtDesc(chatRoomId,
                        ChatMessageStatus.READ, userId)
                .orElseThrow(() -> new BaseException(CHATMESSAGE_NOT_FOUND))
                .toModel();
    }

    // Page number, size에 맞는 메시지 슬라이스 가져오기
    @Override
    public Slice<ChatMessage> findSliceMessages(Long chatRoomId, Pageable pageable) {
        return chatMessageJpaRepository.findByChatRoomIdOrderByCreatedAtDesc(chatRoomId, pageable)
                .map(ChatMessageEntity::toModel);
    }

    @Override
    public Long countByChatRoomId(Long chatRoomId) {
        return chatMessageJpaRepository.countByChatRoomId(chatRoomId);
    }


}

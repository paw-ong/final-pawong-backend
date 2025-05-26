package kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa;

import java.util.List;
import kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa.entity.ChatRoomEntity;
import kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa.repository.ChatRoomJpaRepository;
import kr.co.pawong.pwbe.chat.application.port.out.ChatRoomDataQueryPort;
import kr.co.pawong.pwbe.chat.domain.ChatRoom;
import kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaChatRoomDataQueryAdapter implements ChatRoomDataQueryPort {

    private final ChatRoomJpaRepository chatRoomJpaRepository;

    // user가 participant일 수도, author일 수도 있으므로 동일 param으로 전송
    // 내부적으로는 각각의 userId와 authorId를 받도록 하므로 이렇게 활용해야 함
    @Override
    public List<ChatRoom> findChatRoomsByUserId(Long userId) {
        List<ChatRoomEntity> chatRoomEntities = chatRoomJpaRepository.findAllByParticipantIdOrAuthorId(
                userId, userId);
        return chatRoomEntities.stream()
                .map(ChatRoomEntity::toModel)
                .toList();
    }

    @Override
    public ChatRoom findChatRoomByIdOrThrow(Long chatRoomId) {
        ChatRoomEntity chatRoomEntity = chatRoomJpaRepository.findById(chatRoomId)
                .orElseThrow(() -> new BaseException(CustomErrorCode.CHATROOM_NOT_FOUND));

        return chatRoomEntity.toModel();
    }

    @Override
    public ChatRoom findChatRoomByParticipantIdAndPostIdOrThrow(Long participantId, Long postId) {
        ChatRoomEntity chatRoomEntity = chatRoomJpaRepository.findByParticipantIdAndPostId(
                participantId, postId).orElseThrow(() -> new BaseException(CustomErrorCode.CHATROOM_NOT_FOUND));

        return chatRoomEntity.toModel();
    }
}

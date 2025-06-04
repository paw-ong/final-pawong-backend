package kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa.repository;

import java.util.List;
import java.util.Optional;
import kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa.entity.ChatMessageEntity;
import kr.co.pawong.pwbe.chat.enums.ChatMessageStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessageEntity, Long> {
    
    // 가장 마지막으로 생성된 ChatMessageEntity 갖고오기
    Optional<ChatMessageEntity> findFirstByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);
    // 채팅방ID로 메시지를 최근 생성 순으로 갖고오기 (epoch : asc)
    List<ChatMessageEntity> findChatMessagesByChatRoomIdOrderByCreatedAtAsc(Long chatRoomId);

    Optional<ChatMessageEntity>
    findFirstByChatRoomIdAndStatusAndSenderIdNotOrderByCreatedAtDesc(
            Long chatRoomId,
            ChatMessageStatus status,
            Long readerId
    );

    Slice<ChatMessageEntity> findByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId, Pageable pageable);

    Long countByChatRoomId(Long chatRoomId);
}

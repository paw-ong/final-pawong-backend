package kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa.repository;

import java.util.List;
import kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessageEntity, Long> {

    // 채팅방ID로 메시지를 최근 생성 순으로 갖고오기
    List<ChatMessageEntity> findChatMessagesByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);
}

package kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa.repository;

import java.util.List;
import kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessageEntity, Long> {

    List<ChatMessageEntity> findChatMessagesByChatRoomId(Long chatRoomId);
}

package kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa.repository;

import java.util.List;
import kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoomEntity, Long> {

    List<ChatRoomEntity> findAllByParticipantIdOrAuthorId(Long participantId, Long authorId);
}

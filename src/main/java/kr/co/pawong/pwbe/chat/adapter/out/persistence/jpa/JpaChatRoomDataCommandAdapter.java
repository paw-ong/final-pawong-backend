package kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa;

import kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa.entity.ChatRoomEntity;
import kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa.repository.ChatRoomJpaRepository;
import kr.co.pawong.pwbe.chat.application.port.out.ChatRoomDataCommandPort;
import kr.co.pawong.pwbe.chat.domain.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaChatRoomDataCommandAdapter implements ChatRoomDataCommandPort {

    private final ChatRoomJpaRepository chatRoomJpaRepository;
    /**
     * @param chatRoom
     * @return 생성된 chatRoomId
     */
    @Override
    public Long saveChatRoom(ChatRoom chatRoom) {
        ChatRoomEntity saved = chatRoomJpaRepository.save(ChatRoomEntity.from(chatRoom));
        return saved.getChatRoomId();
    }
}

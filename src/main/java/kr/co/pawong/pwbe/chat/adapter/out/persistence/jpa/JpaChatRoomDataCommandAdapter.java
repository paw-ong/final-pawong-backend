package kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa;

import kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa.entity.ChatRoomEntity;
import kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa.repository.ChatRoomJpaRepository;
import kr.co.pawong.pwbe.chat.application.port.out.ChatRoomDataCommandPort;
import kr.co.pawong.pwbe.chat.domain.ChatRoom;
import kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
        try {
            ChatRoomEntity savedChatRoom = chatRoomJpaRepository.save(
                    ChatRoomEntity.from(chatRoom));
            return savedChatRoom.getChatRoomId();
        } catch (DataIntegrityViolationException e) {
            throw new BaseException(CustomErrorCode.CHATROOM_POST_ERROR);
        }
    }

    /**
     * @param chatRoomId
     * @return 비활성화 여부 boolean
     */
    @Override
    public boolean deactivateChatRoom(Long chatRoomId) {

        ChatRoomEntity chatRoomEntity = chatRoomJpaRepository.findById(chatRoomId)
                .orElseThrow(() -> new BaseException(CustomErrorCode.CHATROOM_NOT_FOUND));

        chatRoomEntity.deactivate();
        return true;
    }
}

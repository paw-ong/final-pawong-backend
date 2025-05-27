package kr.co.pawong.pwbe.chat.application.service;

import kr.co.pawong.pwbe.chat.adapter.in.api.dto.request.ChatRoomCreateRequest;
import kr.co.pawong.pwbe.chat.application.port.in.CommandChatRoomDataUseCase;
import kr.co.pawong.pwbe.chat.application.port.in.QueryChatRoomDataUseCase;
import kr.co.pawong.pwbe.chat.application.port.out.ChatRoomDataCommandPort;
import kr.co.pawong.pwbe.chat.application.port.out.ChatRoomDataQueryPort;
import kr.co.pawong.pwbe.chat.domain.ChatRoom;
import kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommandChatRoomDataService implements CommandChatRoomDataUseCase {

    private final ChatRoomDataCommandPort chatRoomDataCommandPort;
    private final ChatRoomDataQueryPort chatRoomDataQueryPort;
    private final QueryChatRoomDataUseCase queryChatRoomDataUseCase;

    // 생성된 채팅방 id를 반환
    @Override
    @Transactional
    public Long createChatRoomOrFindExistingChatRoom(Long participantId,
            ChatRoomCreateRequest request) {
        try {
            ChatRoom chatRoom = chatRoomDataQueryPort.findChatRoomByParticipantIdAndPostIdOrThrow(
                    participantId,
                    request.getPostId());
            return chatRoom.getChatRoomId();
        } catch (BaseException e) {
            return chatRoomDataCommandPort.saveChatRoomOrThrow(
                    ChatRoom.from(participantId, request));
        }
    }

    // 채팅방 status를 INACTIVE로 변경
    // 사용자가 채팅방에 속하지 않는 경우 예외 발생
    @Override
    @Transactional
    public boolean deactivateChatRoomOrThrow(Long userId, Long chatRoomId) {
        if (!queryChatRoomDataUseCase.userExistsInChatRoom(userId, chatRoomId)) {
            throw new BaseException(CustomErrorCode.FORBIDDEN_CHATROOM_DEACTIVATION);
        }
        return chatRoomDataCommandPort.deactivateChatRoomOrThrow(chatRoomId);
    }
}
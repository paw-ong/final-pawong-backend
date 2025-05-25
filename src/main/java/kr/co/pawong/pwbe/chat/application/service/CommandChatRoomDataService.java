package kr.co.pawong.pwbe.chat.application.service;

import kr.co.pawong.pwbe.chat.adapter.in.api.dto.request.ChatRoomCreateRequest;
import kr.co.pawong.pwbe.chat.application.port.in.CommandChatRoomDataUseCase;
import kr.co.pawong.pwbe.chat.application.port.in.QueryChatRoomDataUseCase;
import kr.co.pawong.pwbe.chat.application.port.out.ChatRoomDataCommandPort;
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
    private final QueryChatRoomDataUseCase queryChatRoomDataUseCase;

    // 생성된 채팅방 id를 반환
    @Override
    @Transactional
    public Long createChatRoom(Long participantId, ChatRoomCreateRequest request) {
        return chatRoomDataCommandPort.saveChatRoomOrThrow(ChatRoom.from(participantId, request));
    }

    @Override
    @Transactional
    public boolean deactivateChatRoom(Long userId, Long chatRoomId) {
        if (queryChatRoomDataUseCase.userExistsInChatRoom(userId, chatRoomId)) {
            return chatRoomDataCommandPort.deactivateChatRoomOrThrow(chatRoomId);
        }
        throw new BaseException(CustomErrorCode.FORBIDDEN_CHATROOM_DEACTIVATION);
    }
}
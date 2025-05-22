package kr.co.pawong.pwbe.chat.application.service;

import kr.co.pawong.pwbe.chat.application.port.in.CommandChatMessageDataUseCase;
import kr.co.pawong.pwbe.chat.application.port.in.QueryChatRoomDataUseCase;
import kr.co.pawong.pwbe.chat.application.port.out.ChatMessageDataCommandPort;
import kr.co.pawong.pwbe.chat.domain.ChatMessage;
import kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommandChatMessageDataService implements CommandChatMessageDataUseCase {

    private final ChatMessageDataCommandPort chatMessageDataCommandPort;
    private final QueryChatRoomDataUseCase queryChatRoomDataUseCase;

    @Override
    public Long createChatMessage(Long chatRoomId, Long senderId, String content) {
        // 유저가 해당 채팅방에 속해있고, 채팅방이 ACTIVE라면 메시지 전송
        if (queryChatRoomDataUseCase.userExistsInChatRoom(senderId, chatRoomId)
                && queryChatRoomDataUseCase.chatRoomIsActive(chatRoomId)) {
            return chatMessageDataCommandPort.saveChatMessage(
                    ChatMessage.from(chatRoomId, senderId, content));
            // 유저가 채팅방에 없다면 메시지 전송 권한이 없음
        } else {
            throw new BaseException(CustomErrorCode.FORBIDDEN_CHATMESSAGE_SENDING);
        }
    }
}

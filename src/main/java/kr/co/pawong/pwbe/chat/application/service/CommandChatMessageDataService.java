package kr.co.pawong.pwbe.chat.application.service;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.*;

import kr.co.pawong.pwbe.chat.application.listener.event.ChatMessageCreatedEvent;
import kr.co.pawong.pwbe.chat.application.port.in.CommandChatMessageDataUseCase;
import kr.co.pawong.pwbe.chat.application.port.in.QueryChatRoomDataUseCase;
import kr.co.pawong.pwbe.chat.application.port.out.ChatMessageDataCommandPort;
import kr.co.pawong.pwbe.chat.domain.ChatMessage;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommandChatMessageDataService implements CommandChatMessageDataUseCase {

    private final ChatMessageDataCommandPort chatMessageDataCommandPort;
    private final QueryChatRoomDataUseCase queryChatRoomDataUseCase;
    private final ApplicationEventPublisher publisher;

    @Override
    public ChatMessage createChatMessage(Long chatRoomId, Long senderId, String content) {
        // 유저가 해당 채팅방에 속해있고, 채팅방이 ACTIVE라면 메시지 전송
        if (queryChatRoomDataUseCase.userExistsInChatRoom(senderId, chatRoomId)
                && queryChatRoomDataUseCase.chatRoomIsActive(chatRoomId)) {
            ChatMessage chatMessage = chatMessageDataCommandPort.saveChatMessage(
                    ChatMessage.from(chatRoomId, senderId, content));
            return chatMessage;
            // 유저가 채팅방에 없거나, 채팅방이 비활성화된 경우 메시지를 보낼 권한이 없음
            // TODO: 채팅방 status 관한 정책 확립
        } else {
            throw new BaseException(FORBIDDEN_CHATMESSAGE_SENDING);
        }
    }

    @Override
    @Transactional
    public void markAllAsRead(Long roomId, Long userId) {
        if (!queryChatRoomDataUseCase.userExistsInChatRoom(userId, roomId)
            || !queryChatRoomDataUseCase.chatRoomIsActive(roomId)) {
            throw new BaseException(FORBIDDEN_CHATMESSAGE_QUERY);
        }

        chatMessageDataCommandPort.readChatMessage(roomId, userId);
    }
}

package kr.co.pawong.pwbe.chat.application.service;

import java.util.List;
import java.util.stream.Collectors;
import kr.co.pawong.pwbe.chat.application.port.in.QueryChatMessageDataUseCase;
import kr.co.pawong.pwbe.chat.application.port.in.QueryChatRoomDataUseCase;
import kr.co.pawong.pwbe.chat.application.port.in.dto.ChatMessageDetail;
import kr.co.pawong.pwbe.chat.application.port.out.ChatMessageDataQueryPort;
import kr.co.pawong.pwbe.chat.domain.ChatMessage;
import kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueryChatMessageDataService implements QueryChatMessageDataUseCase {

    private final QueryChatRoomDataUseCase queryChatRoomDataUseCase;
    private final ChatMessageDataQueryPort chatMessageDataQueryPort;

    @Override
    public List<ChatMessageDetail> findAllMessagesInChatRoom(Long userId, Long chatRoomId) {
        // 유저가 해당 채팅방에 속하지 않다면 예외를 발생
        if (!queryChatRoomDataUseCase.isUserInChatRoom(userId, chatRoomId)) {
            throw new BaseException(CustomErrorCode.FORBIDDEN_CHATMESSAGE_QUERY);
        }

        List<ChatMessage> chatMessages = chatMessageDataQueryPort
                .findChatMessagesByChatRoomIdInLatestOrder(chatRoomId);

        return chatMessages.stream()
                .map(ChatMessageDetail::from)
                .collect(Collectors.toList());
    }
}

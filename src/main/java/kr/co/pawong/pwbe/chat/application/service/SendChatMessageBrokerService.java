package kr.co.pawong.pwbe.chat.application.service;

import kr.co.pawong.pwbe.chat.adapter.in.messaging.dto.request.ChatMessageCreateRequest;
import kr.co.pawong.pwbe.chat.application.port.in.CommandChatMessageDataUseCase;
import kr.co.pawong.pwbe.chat.application.port.in.SendChatMessageBrokerUseCase;
import kr.co.pawong.pwbe.chat.application.port.in.dto.ChatMessageDetail;
import kr.co.pawong.pwbe.chat.application.port.out.ChatMessageBrokerPort;
import kr.co.pawong.pwbe.chat.application.port.out.ChatRoomDataQueryPort;
import kr.co.pawong.pwbe.chat.domain.ChatMessage;
import kr.co.pawong.pwbe.chat.domain.ChatRoom;
import kr.co.pawong.pwbe.user.application.port.out.UserDataQueryPort;
import kr.co.pawong.pwbe.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SendChatMessageBrokerService implements SendChatMessageBrokerUseCase {

    private final CommandChatMessageDataUseCase commandChatMessageDataUseCase;
    private final ChatRoomDataQueryPort chatRoomDataQueryPort;
    private final UserDataQueryPort userDataQueryPort;
    private final ChatMessageBrokerPort chatMessageBrokerPort;

    @Override
    public void createAndSendChatMessage(ChatMessageCreateRequest request, Long chatRoomId,
            Long userId) {
        ChatMessage chatMessage = createChatMessage(request, chatRoomId, userId);
        sendChatMessageToUsersInChatRoom(chatRoomId, chatMessage);
    }

    private void sendChatMessageToUsersInChatRoom(Long chatRoomId, ChatMessage chatMessage) {
        ChatMessageDetail messageDetail = ChatMessageDetail.from(chatMessage);

        /* find user in chat room */
        ChatRoom chatRoom = chatRoomDataQueryPort.findChatRoomByIdOrThrow(chatMessage.getChatRoomId());
        User author = userDataQueryPort.findByUserIdOrThrow(chatRoom.getAuthorId());
        User participant = userDataQueryPort.findByUserIdOrThrow(chatRoom.getParticipantId());

        /* send it to users */
        String userDest = "/queue/chat/" + chatRoomId;
        chatMessageBrokerPort.sendMessageToUser(String.valueOf(author.getSocialId()), userDest, messageDetail);
        chatMessageBrokerPort.sendMessageToUser(String.valueOf(participant.getSocialId()), userDest,messageDetail);
    }

    private ChatMessage createChatMessage(ChatMessageCreateRequest request, Long chatRoomId,
            Long userId) {
        return commandChatMessageDataUseCase.createChatMessage(
                chatRoomId,
                userId,
                request.getContent());
    }
}

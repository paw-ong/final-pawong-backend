package kr.co.pawong.pwbe.chat.application.service;

import kr.co.pawong.pwbe.chat.adapter.in.api.dto.request.ChatRoomCreateRequest;
import kr.co.pawong.pwbe.chat.application.port.in.CommandChatRoomDataUseCase;
import kr.co.pawong.pwbe.chat.application.port.out.ChatRoomDataCommandPort;
import kr.co.pawong.pwbe.chat.domain.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommandChatRoomDataService implements CommandChatRoomDataUseCase {

    private final ChatRoomDataCommandPort chatRoomDataCommandPort;

    // 생성된 채팅방 id를 반환
    @Override
    public Long createChatRoom(Long senderId, ChatRoomCreateRequest request) {
        return chatRoomDataCommandPort.saveChatRoom(ChatRoom.from(senderId, request));
    }
}
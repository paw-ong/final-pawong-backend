package kr.co.pawong.pwbe.chat.application.port.in;

import kr.co.pawong.pwbe.chat.adapter.in.api.dto.request.ChatRoomCreateRequest;

// 채팅방 생성을 위한 usecase
public interface CommandChatRoomDataUseCase {

    /**
     * param 채팅방_생성_요청자ID, 게시글ID
     * return 생성된 채팅방ID 혹은 이미 존재하는 채팅방ID
     */
    Long createChatRoomOrFindExistingChatRoom(Long participantId, ChatRoomCreateRequest request);

    boolean deactivateChatRoomOrThrow(Long userId, Long chatRoomId);

}

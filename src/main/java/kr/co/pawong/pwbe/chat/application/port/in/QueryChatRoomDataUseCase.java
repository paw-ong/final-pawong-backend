package kr.co.pawong.pwbe.chat.application.port.in;

import java.util.List;
import kr.co.pawong.pwbe.chat.application.port.in.dto.ChatRoomDetail;

public interface QueryChatRoomDataUseCase {

    List<ChatRoomDetail> findAllChatRooms(Long userId);

    boolean userExistsInChatRoom(Long userId, Long chatRoomId);

    boolean chatRoomIsActive(Long chatRoomId);
}

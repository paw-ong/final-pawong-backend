package kr.co.pawong.pwbe.chat.application.port.in;

import java.util.List;
import kr.co.pawong.pwbe.chat.application.port.in.dto.ChatRoomDetail;

public interface QueryChatRoomDataUseCase {

    List<ChatRoomDetail> findUserChatRooms(Long userId);

    List<ChatRoomDetail> findUserChatRoomsByPostId(Long userId, Long postId);

    boolean isUserInChatRoom(Long userId, Long chatRoomId);

    boolean isChatRoomActive(Long chatRoomId);
}

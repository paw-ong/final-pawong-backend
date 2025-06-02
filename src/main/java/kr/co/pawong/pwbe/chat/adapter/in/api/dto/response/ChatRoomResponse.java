package kr.co.pawong.pwbe.chat.adapter.in.api.dto.response;

import kr.co.pawong.pwbe.chat.application.port.in.dto.ChatRoomDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRoomResponse {

    private ChatRoomDetail chatRoomDetail;
}

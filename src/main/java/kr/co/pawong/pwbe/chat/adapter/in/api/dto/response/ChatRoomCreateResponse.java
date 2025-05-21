package kr.co.pawong.pwbe.chat.adapter.in.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatRoomCreateResponse {

    private Long chatRoomId;    // 생성된 채팅방 ID
}

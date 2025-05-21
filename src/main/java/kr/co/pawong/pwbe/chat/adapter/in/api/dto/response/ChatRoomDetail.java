package kr.co.pawong.pwbe.chat.adapter.in.api.dto.response;

import kr.co.pawong.pwbe.lostPost.application.port.in.dto.ChatRoomLostPostInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO: move package
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDetail {

    private ChatRoomLostPostInfo lostPostInfo;
    private Long chatRoomId;
}

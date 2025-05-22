package kr.co.pawong.pwbe.chat.application.port.in.dto;

import kr.co.pawong.pwbe.lostPost.application.port.in.dto.ChatRoomLostPostInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDetail {

    private ChatRoomLostPostInfo lostPostInfo;
    private Long chatRoomId;
}

package kr.co.pawong.pwbe.chat.application.port.in.dto;

import kr.co.pawong.pwbe.chat.adapter.out.lostPost.dto.ChatRoomLostPostInfo;
import kr.co.pawong.pwbe.chat.enums.ChatRoomStatus;
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
    private Long participantId;
    private String participantUserName;
    private ChatRoomStatus status;
    private String latestMessageContent;
}

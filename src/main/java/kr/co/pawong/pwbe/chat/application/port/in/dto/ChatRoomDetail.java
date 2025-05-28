package kr.co.pawong.pwbe.chat.application.port.in.dto;

import kr.co.pawong.pwbe.chat.adapter.out.lostPost.dto.ChatRoomLostPostInfo;
import kr.co.pawong.pwbe.chat.domain.ChatRoom;
import kr.co.pawong.pwbe.chat.enums.ChatRoomStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class ChatRoomDetail {

    private ChatRoomLostPostInfo lostPostInfo;
    private Long chatRoomId;
    private Long participantId;
    private String participantUserName;
    private ChatRoomStatus status;
    private String latestMessageContent;

    public static ChatRoomDetail of(ChatRoom chatRoom, ChatRoomLostPostInfo lostPostInfo,
            String participantUserName, String latestMessageContent) {
        return ChatRoomDetail.builder()
                .lostPostInfo(lostPostInfo)
                .chatRoomId(chatRoom.getChatRoomId())
                .participantId(chatRoom.getParticipantId())
                .participantUserName(participantUserName)
                .status(chatRoom.getStatus())
                .latestMessageContent(latestMessageContent).build();
    }
}

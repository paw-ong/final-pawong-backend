package kr.co.pawong.pwbe.chat.domain;

import java.time.LocalDateTime;
import kr.co.pawong.pwbe.chat.enums.ChatMessageStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessage {

    private Long messageId;     // 메시지 id
    private Long chatRoomId;    // 채팅방 id
    private Long senderId;      // 발신자 id
    private String content;     // 내용
    private LocalDateTime createdAt;    // 생성날짜
    private LocalDateTime readAt;       // 읽음 처리된 날짜
    private ChatMessageStatus status;   // SENT, READ

    public static ChatMessage from(Long chatRoomId, Long senderId, String content) {
        return ChatMessage.builder()
                .chatRoomId(chatRoomId)
                .senderId(senderId)
                .content(content)
                .createdAt(LocalDateTime.now())
                .status(ChatMessageStatus.SENT)
                .build();
    }
}
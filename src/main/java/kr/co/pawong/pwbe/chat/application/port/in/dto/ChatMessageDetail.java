package kr.co.pawong.pwbe.chat.application.port.in.dto;

import java.time.LocalDateTime;
import kr.co.pawong.pwbe.chat.domain.ChatMessage;
import kr.co.pawong.pwbe.chat.enums.ChatMessageStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDetail {

    private String content;     // 메시지 내용
    private Long senderId;      // 누가 보냈는지 (활용가능)
    private ChatMessageStatus status;   // 읽음 여부 (언제 읽었는지는 일단 활용X)
    private LocalDateTime createdAt;    // 언제 보냈는지

    public static ChatMessageDetail from(ChatMessage chatMessage) {
        return ChatMessageDetail.builder()
                .content(chatMessage.getContent())
                .senderId(chatMessage.getSenderId())
                .status(chatMessage.getStatus())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }
}

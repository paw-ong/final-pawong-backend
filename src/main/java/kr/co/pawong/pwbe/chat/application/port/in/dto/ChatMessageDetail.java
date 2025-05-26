package kr.co.pawong.pwbe.chat.application.port.in.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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

    private Long chatMessageId;
    private String content;     // 메시지 내용
    private Long senderId;      // 누가 보냈는지 (활용가능)
    private ChatMessageStatus status;   // 읽음 여부 (언제 읽었는지는 일단 활용X)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createdAt; // Instant -> Long (에폭크 타임) -> String -> JSON

    public static ChatMessageDetail from(ChatMessage chatMessage) {
        return ChatMessageDetail.builder()
                .chatMessageId(chatMessage.getMessageId())
                .content(chatMessage.getContent())
                .senderId(chatMessage.getSenderId())
                .status(chatMessage.getStatus())
                .createdAt(chatMessage.getCreatedAt().toEpochMilli())
                .build();
    }
}

package kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import kr.co.pawong.pwbe.chat.domain.ChatMessage;
import kr.co.pawong.pwbe.chat.enums.ChatMessageStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ChatMessage")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @Column(name = "chat_room_id", nullable = false)
    private Long chatRoomId;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private Instant createdAt;

    private Instant readAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatMessageStatus status;   // SENT, READ

    public static ChatMessageEntity from(ChatMessage chatMessage) {
        ChatMessageEntity entity = new ChatMessageEntity();
        entity.messageId = chatMessage.getMessageId();
        entity.chatRoomId = chatMessage.getChatRoomId();
        entity.senderId = chatMessage.getSenderId();
        entity.content = chatMessage.getContent();
        entity.createdAt = chatMessage.getCreatedAt();
        entity.readAt = chatMessage.getReadAt();
        entity.status = chatMessage.getStatus();
        return entity;
    }

    public ChatMessage toModel() {
        return ChatMessage.builder()
                .messageId(this.messageId)
                .chatRoomId(this.chatRoomId)
                .senderId(this.senderId)
                .content(this.content)
                .createdAt(this.createdAt)
                .readAt(this.readAt)
                .status(this.status)
                .build();
    }
}
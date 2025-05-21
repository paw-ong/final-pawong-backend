package kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import kr.co.pawong.pwbe.chat.domain.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "ChatRoom",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_chat_room_post_sender",
                columnNames = {"postId", "senderId"}
        )
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    @Column(nullable = false)
    private Long postId;    // 게시글 id

    @Column(nullable = false)
    private Long authorId;  // 게시글 작성자 id

    @Column(nullable = false)
    private Long senderId;  // 채팅 요청자 id

    @Column(nullable = false)
    private LocalDateTime createdAt;    // 생성날짜

    // TODO: status?

    public static ChatRoomEntity from(ChatRoom chatRoom) {
        ChatRoomEntity entity = new ChatRoomEntity();
        entity.chatRoomId = chatRoom.getChatRoomId();
        entity.postId = chatRoom.getPostId();
        entity.authorId = chatRoom.getAuthorId();
        entity.senderId = chatRoom.getSenderId();
        entity.createdAt = chatRoom.getCreatedAt();
        return entity;
    }

    public ChatRoom toModel() {
        return ChatRoom.builder()
                .chatRoomId(this.chatRoomId)
                .postId(this.postId)
                .authorId(this.authorId)
                .senderId(this.senderId)
                .createdAt(this.createdAt)
                .build();
    }
}
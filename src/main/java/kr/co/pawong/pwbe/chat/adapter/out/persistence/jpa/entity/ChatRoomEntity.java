package kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import kr.co.pawong.pwbe.chat.domain.ChatRoom;
import kr.co.pawong.pwbe.chat.enums.ChatRoomStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(
        name = "ChatRoom",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_chat_room_post_sender",
                columnNames = {"post_id", "sender_id"}
        )
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class ChatRoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    @Column(nullable = false)
    private Long postId;    // 게시글 id

    @Column(nullable = false)
    private Long authorId;  // 게시글 작성자 id

    @Column(nullable = false)
    private Long participantId;  // 채팅 요청자 id

    @Column(nullable = false)
    private LocalDateTime createdAt;    // 생성날짜

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ChatRoomStatus status;  // 채팅방 활성화, 비활성화

    public static ChatRoomEntity from(ChatRoom chatRoom) {
        ChatRoomEntity entity = new ChatRoomEntity();
        entity.chatRoomId = chatRoom.getChatRoomId();
        entity.postId = chatRoom.getPostId();
        entity.authorId = chatRoom.getAuthorId();
        entity.participantId = chatRoom.getParticipantId();
        entity.createdAt = chatRoom.getCreatedAt();
        entity.status = chatRoom.getStatus();
        return entity;
    }

    public ChatRoom toModel() {
        return ChatRoom.builder()
                .chatRoomId(this.chatRoomId)
                .postId(this.postId)
                .authorId(this.authorId)
                .participantId(this.participantId)
                .createdAt(this.createdAt)
                .status(this.status)
                .build();
    }

    // 채팅방 상태 비활성화로 전환
    public void deactivate() {
        this.status = ChatRoomStatus.INACTIVE;
    }
}
package kr.co.pawong.pwbe.chat.domain;

import java.time.LocalDateTime;
import kr.co.pawong.pwbe.chat.adapter.in.api.dto.request.ChatRoomCreateRequest;
import kr.co.pawong.pwbe.chat.enums.ChatRoomStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoom {

    private Long chatRoomId;    // 채팅방 id
    private Long postId;    // 게시글 id
    private Long authorId;  // 게시글 작성자 id
    private Long participantId;  // 채팅 요청자 id
    private LocalDateTime createdAt;    // 생성날짜
    private ChatRoomStatus status;  // ACTIVE, INACTIVE

    public static ChatRoom from(Long participantId, ChatRoomCreateRequest request) {
        return ChatRoom.builder()
                .participantId(participantId)
                .authorId(request.getAuthorId())
                .postId(request.getPostId())
                .createdAt(LocalDateTime.now())
                .status(ChatRoomStatus.ACTIVE)
                .build();
    }

    public boolean userExistsInChatRoom(Long userId) {
        return this.participantId.equals(userId) || this.authorId.equals(userId);
    }

    public boolean isActive() {
        return this.status == ChatRoomStatus.ACTIVE;
    }
}
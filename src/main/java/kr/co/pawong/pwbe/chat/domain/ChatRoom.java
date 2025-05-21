package kr.co.pawong.pwbe.chat.domain;

import java.time.LocalDateTime;
import kr.co.pawong.pwbe.chat.adapter.in.api.dto.request.ChatRoomCreateRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoom {
    private Long chatRoomId;    // 채팅방 id
    private Long postId;    // 게시글 id
    private Long authorId;  // 게시글 작성자 id
    private Long senderId;  // 채팅 요청자 id
    private LocalDateTime createdAt;    // 생성날짜
    // TODO: status

    public static ChatRoom from(Long senderId, ChatRoomCreateRequest request) {
        return ChatRoom.builder()
                .senderId(senderId)
                .authorId(request.getAuthorId())
                .postId(request.getPostId())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
package kr.co.pawong.pwbe.chat.adapter.in.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatRoomCreateRequest {

    private Long postId;    // 게시글 ID
    private Long authorId;  // 게시글 작성자 ID
}

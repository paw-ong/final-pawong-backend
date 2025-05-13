package kr.co.pawong.pwbe.lostPost.adapter.in.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LostPostCreateResponse {

    private Long lostPostId;        // 실종게시글 id
}

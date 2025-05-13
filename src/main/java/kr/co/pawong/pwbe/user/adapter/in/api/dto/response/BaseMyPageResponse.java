package kr.co.pawong.pwbe.user.adapter.in.api.dto.response;

import java.util.List;

/**
 * 마이페이지 목록들 반환에 사용할 Base Response
 *
 * @param content - 항목 리스트
 * @param <T> - 전달할 항목
 */
public record BaseMyPageResponse<T>(
        List<T> content
) {

}

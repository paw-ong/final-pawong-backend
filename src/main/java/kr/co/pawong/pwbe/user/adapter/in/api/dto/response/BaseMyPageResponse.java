package kr.co.pawong.pwbe.user.adapter.in.api.dto.response;

import java.util.List;

public record BaseMyPageResponse<T>(
        List<T> content
) {}

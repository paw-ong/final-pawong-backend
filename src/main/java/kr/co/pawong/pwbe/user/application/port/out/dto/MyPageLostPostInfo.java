package kr.co.pawong.pwbe.user.application.port.out.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record MyPageLostPostInfo(
        Long postId,
        String postType,
        String author,
        LocalDate happenedDate,
        String happenedPlace,
        String upKindNm,
        String kindNm,
        LocalDateTime createdAt,
        String feature
) {}

package kr.co.pawong.pwbe.lostPost.application.port.in.dto;

import lombok.Builder;

@Builder
public record LostAnimalQuery(
        LostType type,
        Long id, // lostPostId 또는 adoptionId
        Long userId
) {

    public enum LostType {
        LOST_ADOPTION, LOST_POST
    }

}

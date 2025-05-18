package kr.co.pawong.pwbe.lostPost.application.port.in.dto;

import lombok.Builder;

@Builder
public record LostAnimalQuery(
        LostType type,
        long id, // lostPostId 또는 adoptionId
        long userId
) {

    public enum LostType {
        LOST_ADOPTION, LOST_POST
    }

}

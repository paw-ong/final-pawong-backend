package kr.co.pawong.pwbe.lostPost.application.port.in.dto;

import kr.co.pawong.pwbe.lostPost.enums.PostType;

public record LostAnimalEngineResponse(
        long id,
        PostType type
) {

}

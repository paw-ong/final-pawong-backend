package kr.co.pawong.pwbe.lostPost.application.port.out.dto;

import java.util.List;
import kr.co.pawong.pwbe.lostPost.enums.PostType;

public record LostAnimalEngineRequest(
        List<PostType> types,
        float[] embedding
) {

}

package kr.co.pawong.pwbe.lostPost.application.port.in;

import java.util.List;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;

public interface SearchSimilarLostAnimalsUseCase {

    List<LostPostCard> searchSimilarLostAnimals(Long userId, Long lostPostId);
}

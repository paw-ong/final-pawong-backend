package kr.co.pawong.pwbe.lostPost.application.port.in;

import java.util.List;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostAnimalQuery;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;

public interface QueryLostAnimalDataUseCase {

    List<LostPostCard> getLostAnimalsByIds(List<LostAnimalQuery> lostAnimalQueries);
}

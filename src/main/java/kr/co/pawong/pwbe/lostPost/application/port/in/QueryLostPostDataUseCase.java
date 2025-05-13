package kr.co.pawong.pwbe.lostPost.application.port.in;

import java.util.List;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;

public interface QueryLostPostDataUseCase {

    List<LostPostCard> getLostPostsByUserId(Long userId);
}

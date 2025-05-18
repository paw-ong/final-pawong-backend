package kr.co.pawong.pwbe.user.application.port.out;

import java.util.List;
import kr.co.pawong.pwbe.user.application.port.out.dto.MyPageLostPostInfo;
import kr.co.pawong.pwbe.user.domain.LostBookmark;

public interface LostPostInfoPort {

    List<MyPageLostPostInfo> getLostPostsByUserId(Long userId);

    List<MyPageLostPostInfo> getLostAnimalsByIds(List<LostBookmark> lostIds);
}

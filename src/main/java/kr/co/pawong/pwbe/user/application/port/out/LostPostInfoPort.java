package kr.co.pawong.pwbe.user.application.port.out;

import java.util.List;
import kr.co.pawong.pwbe.user.application.port.out.dto.MyPageLostPostInfo;

public interface LostPostInfoPort {
    List<MyPageLostPostInfo> getLostPostsByUserId(Long userId);
}

package kr.co.pawong.pwbe.user.application.port.in;

import java.util.List;
import kr.co.pawong.pwbe.user.application.port.in.dto.MyPageLostPostResponse;

public interface QueryMyPageDataUseCase {

    List<MyPageLostPostResponse> getLostPostsByUserId(Long userId);

}

package kr.co.pawong.pwbe.lostPost.application.port.in.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SliceLostPostSearchResponses {

    private boolean hasNext; // 다음 페이지가 있는지 여부
    private List<LostPostCard> lostPostCards;

}

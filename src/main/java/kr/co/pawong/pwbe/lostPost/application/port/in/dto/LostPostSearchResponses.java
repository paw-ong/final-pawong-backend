package kr.co.pawong.pwbe.lostPost.application.port.in.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LostPostSearchResponses {
    private boolean hasNext;
    private List<LostPostCard> lostPostCards;
}

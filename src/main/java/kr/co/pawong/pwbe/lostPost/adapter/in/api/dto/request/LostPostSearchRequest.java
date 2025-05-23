package kr.co.pawong.pwbe.lostPost.adapter.in.api.dto.request;

import java.util.List;
import kr.co.pawong.pwbe.adoption.enums.SexCd;
import kr.co.pawong.pwbe.adoption.enums.UpKindCd;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LostPostSearchRequest {
    private PostType type; // ex: type=LOST
    private List<UpKindCd> upKindCds; // ex: upKindCds=KIND1&upKindCds=KIND2
    private SexCd sexCd; // ex: sexCd=FEMALE
    private List<String> regions;     // ex: regions=서울&regions=부산

}

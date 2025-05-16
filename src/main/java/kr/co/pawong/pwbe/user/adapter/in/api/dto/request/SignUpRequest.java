package kr.co.pawong.pwbe.user.adapter.in.api.dto.request;

import kr.co.pawong.pwbe.user.application.port.in.dto.UserUpdate;
import lombok.Getter;

@Getter
public class SignUpRequest {

    private String nickname;
    private String region;
    private String tel;

    public UserUpdate update() {
        return UserUpdate.builder()
                .nickname(nickname)
                .region(region)
                .tel(tel)
                .build();
    }
}

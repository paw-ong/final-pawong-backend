package kr.co.pawong.pwbe.user.application.port.in.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserUpdate {

    private String nickname;
    private String region;
    private String tel;

}

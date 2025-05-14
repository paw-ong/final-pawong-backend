package kr.co.pawong.pwbe.user.application.port.in.dto;

import kr.co.pawong.pwbe.user.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {

    private Long userId;
    private UserStatus status;
}

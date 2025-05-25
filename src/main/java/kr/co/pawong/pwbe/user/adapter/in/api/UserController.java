package kr.co.pawong.pwbe.user.adapter.in.api;

import jakarta.annotation.security.PermitAll;
import kr.co.pawong.pwbe.user.adapter.in.api.dto.response.UserResponse;
import kr.co.pawong.pwbe.user.adapter.out.security.CustomUserDetails;
import kr.co.pawong.pwbe.user.application.port.in.QueryUserDataUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserController {

    private final QueryUserDataUseCase queryUserDataUseCase;

    @PermitAll
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getUser(
            Authentication authentication
    ) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        Long userId = principal.getUserId();
        UserResponse userResponse = new UserResponse(queryUserDataUseCase.getUser(userId));

        return ResponseEntity.ok(userResponse);
    }

}

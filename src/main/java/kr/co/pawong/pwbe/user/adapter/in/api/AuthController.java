package kr.co.pawong.pwbe.user.adapter.in.api;

import kr.co.pawong.pwbe.user.adapter.out.security.CustomUserDetails;
import kr.co.pawong.pwbe.user.adapter.in.api.dto.request.SignUpRequest;
import kr.co.pawong.pwbe.user.application.port.in.dto.AuthResponse;
import kr.co.pawong.pwbe.user.application.port.in.AuthUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {

  private final AuthUseCase authUseCase;

  @PostMapping("/signup")
  public ResponseEntity<AuthResponse> signUp(
      @RequestBody SignUpRequest signUpRequest,
      Authentication authentication
  ) {
    CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

    Long userId = principal.getUserId();
    return ResponseEntity.ok(authUseCase.signUp(userId, signUpRequest.update()));
  }

}

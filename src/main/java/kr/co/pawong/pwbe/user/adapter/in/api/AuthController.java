package kr.co.pawong.pwbe.user.adapter.in.api;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import kr.co.pawong.pwbe.global.security.dto.CustomUserDetails;
import kr.co.pawong.pwbe.user.adapter.in.api.dto.request.SignUpRequest;
import kr.co.pawong.pwbe.user.application.port.in.AuthUseCase;
import kr.co.pawong.pwbe.user.application.port.in.dto.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.oauth2.jwt.JwtException;
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
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(authUseCase.signUp(
                userDetails.getUserId(),
                signUpRequest.update()));
    }

    @GetMapping("/csrf-token")
    public ResponseEntity<Map<String, String>> csrf(CsrfToken model)  {
        return ResponseEntity.ok(Map.of("csrf-token", model.getToken()));
    }


}

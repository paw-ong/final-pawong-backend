package kr.co.pawong.pwbe.infrastructure.fcm.adapter.in.api;

import kr.co.pawong.pwbe.infrastructure.fcm.adapter.in.api.dto.FcmTokenRequest;
import kr.co.pawong.pwbe.infrastructure.fcm.application.port.in.FcmUsecase;
import kr.co.pawong.pwbe.user.adapter.out.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/fcm")
@RequiredArgsConstructor
public class FcmController {

    private final FcmUsecase fcmUsecase;

    @PostMapping("/token")
    public ResponseEntity<String> saveToken(@RequestBody FcmTokenRequest request) {
        log.info("FCM 토큰 등록 요청: {}", request.getToken());

        // 현재 인증된 사용자의 ID 조회
        Long userId = getCurrentUserId();

        // FCM 토큰 저장
        fcmUsecase.saveFcmToken(userId, request.getToken());

        return ResponseEntity.ok("FCM 토큰이 성공적으로 등록되었습니다.");
    }

    // 현재 인증된 사용자의 ID 조회
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((CustomUserDetails) authentication.getPrincipal()).getUserId();
    }
}

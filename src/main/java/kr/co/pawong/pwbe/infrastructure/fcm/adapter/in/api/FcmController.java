package kr.co.pawong.pwbe.infrastructure.fcm.adapter.in.api;

import kr.co.pawong.pwbe.infrastructure.fcm.adapter.in.api.dto.FcmTokenRequest;
import kr.co.pawong.pwbe.infrastructure.fcm.application.port.in.FcmUsecase;
import kr.co.pawong.pwbe.global.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<String> saveToken(
            @RequestBody FcmTokenRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        // 현재 인증된 사용자의 ID 조회
        Long userId = customUserDetails.getUserId();

        // FCM 토큰 저장
        fcmUsecase.saveFcmToken(userId, request.getToken());

        return ResponseEntity.ok("FCM 토큰이 성공적으로 등록되었습니다.");
    }
}

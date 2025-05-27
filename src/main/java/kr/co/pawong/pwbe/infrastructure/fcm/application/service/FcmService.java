package kr.co.pawong.pwbe.infrastructure.fcm.application.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import kr.co.pawong.pwbe.infrastructure.fcm.application.port.in.FcmUsecase;
import kr.co.pawong.pwbe.infrastructure.fcm.application.port.out.FcmPort;
import kr.co.pawong.pwbe.infrastructure.fcm.domain.FcmToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService implements FcmUsecase {

    private final FcmPort fcmPort;
    private final FirebaseMessaging firebaseMessaging;

    @Transactional
    @Override
    public void saveFcmToken(Long userId, String token) {
        log.info("FCM 토큰 저장: 사용자={}, 토큰={}", userId, token);

        // 기존 토큰 조회 or 새 토큰 생성
        FcmToken fcmToken = fcmPort.findByUserId(userId);

        // 토큰 정보 업데이트
        fcmToken.updateToken(userId, token);

        // DB에 저장
        fcmPort.save(fcmToken);
        log.info("FCM 토큰 저장 완료: 사용자={}", userId);
    }

    // 사용자 ID로 FCM 토큰 조회
    @Override
    public String getTokenByUserId(Long userId) {
        log.info("사용자 ID로 FCM 토큰 조회: {}", userId);

        return fcmPort.findByUserId(userId).getToken();
    }

    // 사용자 ID로 유효한 FCM 토큰 조회
    @Override
    public String getValidTokenByUserId(Long userId) {
        log.info("사용자 ID로 유효한 FCM 토큰 조회: {}", userId);

        // 저장된 토큰 조회
        String token = getTokenByUserId(userId);
        if (token == null) {
            log.warn("FCM 토큰을 찾을 수 없습니다: userId={}", userId);
            return null;
        }

        // 토큰 유효성 검증
        if (validateFcmToken(token)) {
            log.info("유효한 FCM 토큰 조회 성공: 사용자={}", userId);
            return token;
        } else {
            log.warn("유효하지 않은 FCM 토큰 발견, 삭제 처리: 사용자={}", userId);
            // 유효하지 않은 토큰은 DB에서 삭제
            removeFcmToken(userId);
            return null;
        }
    }

    // FCM 토큰 유효성 검사
    @Override
    public boolean validateFcmToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            log.warn("FCM 토큰이 null이거나 비어있습니다.");
            return false;
        }

        try {
            Message testMessage = Message.builder()
                    .setToken(token)
                    .putData("test", "validation")
                    .build();

            // dry_run=true로 실제 전송 없이 유효성만 검증
            firebaseMessaging.send(testMessage, true);

            log.debug("FCM 토큰 유효성 검사 성공: token={}", token);
            return true;
        } catch (FirebaseMessagingException e) {
            log.warn("FCM 토큰 유효성 검사 실패: token={}, error={}", token, e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("FCM 토큰 유효성 검사 중 예상치 못한 오류: token={}", token, e);
            return false;
        }
    }

    // 유효하지 않은 FCM 토큰 삭제
    private void removeFcmToken(Long userId) {
        log.info("유효하지 않은 FCM 토큰 삭제: 사용자={}", userId);

        try {
            fcmPort.deleteByUserId(userId);
            log.info("FCM 토큰 삭제 완료: 사용자={}", userId);
        } catch (Exception e) {
            log.error("FCM 토큰 삭제 실패: 사용자={}", userId, e);
        }
    }
}

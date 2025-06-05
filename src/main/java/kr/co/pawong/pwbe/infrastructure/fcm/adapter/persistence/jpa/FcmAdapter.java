package kr.co.pawong.pwbe.infrastructure.fcm.adapter.persistence.jpa;

import kr.co.pawong.pwbe.infrastructure.fcm.adapter.persistence.jpa.entity.FcmTokenEntity;
import kr.co.pawong.pwbe.infrastructure.fcm.adapter.persistence.jpa.repository.FcmRepository;
import kr.co.pawong.pwbe.infrastructure.fcm.application.port.out.FcmPort;
import kr.co.pawong.pwbe.infrastructure.fcm.application.service.support.FcmMapper;
import kr.co.pawong.pwbe.infrastructure.fcm.domain.FcmToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FcmAdapter implements FcmPort {

    private final FcmRepository fcmRepository;
    private final FcmMapper fcmMapper;

    @Override
    public void save(FcmToken fcmToken) {
        // FcmToken -> FcmTokenEntity
        FcmTokenEntity entity = fcmMapper.toEntity(fcmToken);
        // DB에 저장
        fcmRepository.save(entity);
        log.info("FCM 토큰 저장 완료: 사용자={}", fcmToken.getUserId());
    }

    // 사용자 ID로 FCM 토큰 조회
    @Override
    public FcmToken findByUserId(Long userId) {
        return fcmRepository.findByUserId(userId)
                .map(fcmMapper::toDomain)
                .orElse(null);
    }

    // 사용자 ID로 FCM 토큰 삭제
    @Override
    public void deleteByUserId(Long userId) {
        log.debug("사용자 ID로 FCM 토큰 삭제: userId={}", userId);
        fcmRepository.deleteByUserId(userId);
    }

    @Override
    public void deleteByToken(String token) {
        fcmRepository.deleteByToken(token);
    }
}

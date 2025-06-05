package kr.co.pawong.pwbe.infrastructure.fcm.application.port.out;

import kr.co.pawong.pwbe.infrastructure.fcm.domain.FcmToken;

public interface FcmPort {
    void save(FcmToken fcmToken);
    FcmToken findByUserId(Long userId);
    void deleteByUserId(Long userId);
    void deleteByToken(String token);
}

package kr.co.pawong.pwbe.infrastructure.fcm.application.port.out;

import java.util.Optional;
import kr.co.pawong.pwbe.infrastructure.fcm.domain.FcmToken;

public interface FcmPort {
    void save(FcmToken fcmToken);
    Optional<FcmToken> findByUserId(Long userId);
    void deleteByUserId(Long userId);

}

package kr.co.pawong.pwbe.infrastructure.fcm.application.port.in;

public interface FcmUsecase {
    void saveFcmToken(Long userId, String token);
    String getTokenByUserId(Long userId);
    String getValidTokenByUserId(Long userId);
    boolean validateFcmToken(String token);
}

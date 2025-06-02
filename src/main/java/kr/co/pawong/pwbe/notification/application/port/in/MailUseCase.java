package kr.co.pawong.pwbe.notification.application.port.in;

import kr.co.pawong.pwbe.notification.application.port.in.dto.NotificationRequest;

public interface MailUseCase {

    void sendCodeToEmail(String toEmail);

    Boolean verifiedCode(String email, String authCode);

    void processMailNotificationMessage(String jsonString);

    void sendSimilarAdoptionEmail(NotificationRequest request);

}

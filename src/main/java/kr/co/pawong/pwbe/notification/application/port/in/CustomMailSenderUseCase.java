package kr.co.pawong.pwbe.notification.application.port.in;

import kr.co.pawong.pwbe.notification.application.port.in.dto.NotificationRequest;

public interface CustomMailSenderUseCase {

    void sendCodeEmail(String toEmail, String title, String text);

    void sendSimilarAdoptionEmail(NotificationRequest request, String title);

}

package kr.co.pawong.pwbe.notification.application.port.in;

import kr.co.pawong.pwbe.notification.application.port.in.dto.NotificationRequest;
import kr.co.pawong.pwbe.notification.application.service.dto.NotificationEmailDto;

public interface CustomMailSenderUseCase {

    void sendCodeEmail(String toEmail, String title, String text);

    void sendSimilarAdoptionEmail(NotificationEmailDto notificationEmailDto);

}

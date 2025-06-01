package kr.co.pawong.pwbe.notification.application.port.in;

public interface CustomMailSenderUseCase {

    void sendEmail(String toEmail, String title, String text);

}

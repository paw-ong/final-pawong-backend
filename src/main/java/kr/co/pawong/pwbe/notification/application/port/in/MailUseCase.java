package kr.co.pawong.pwbe.notification.application.port.in;

public interface MailUseCase {

    void sendCodeToEmail(String toEmail);

    Boolean verifiedCode(String email, String authCode);

}

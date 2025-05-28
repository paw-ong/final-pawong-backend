package kr.co.pawong.pwbe.user.application.port.in;

import org.springframework.mail.SimpleMailMessage;

public interface EmailUseCase {

    void sendEmail(String toEmail, String title, String text);

}

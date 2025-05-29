package kr.co.pawong.pwbe.user.adapter.in.api;

import kr.co.pawong.pwbe.notification.application.port.in.MailUseCase;
import kr.co.pawong.pwbe.user.application.port.in.QueryUserDataUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/mail")
@RestController
public class EmailController {

    private final QueryUserDataUseCase queryUserDataUseCase;
    private final MailUseCase mailUseCase;

    // 인증코드 발송
    @PostMapping("/six")
    public ResponseEntity<?> sendMail(@RequestParam("email") String email) {
        mailUseCase.sendCodeToEmail(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 인증코드 검증
    @GetMapping("/verifications")
    public ResponseEntity<String> verificationEmail(@RequestParam("email") String email,
            @RequestParam("code") String authCode) {
        String response = mailUseCase.verifiedCode(email, authCode);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 이메일 중복 검사
    @GetMapping("/is-email-exist")
    public ResponseEntity<Boolean> isEmailExist(@RequestParam("email") String email) {
        Boolean verification = queryUserDataUseCase.isEmailExist(email);
        return new ResponseEntity<>(verification, HttpStatus.OK);
    }

}

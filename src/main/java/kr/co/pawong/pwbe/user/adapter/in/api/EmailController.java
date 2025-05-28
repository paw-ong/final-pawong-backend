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

    @PostMapping("/six")
    public ResponseEntity sendMessage(@RequestParam("email") String email) {
        mailUseCase.sendCodeToEmail(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/verifications")
    public ResponseEntity verificationEmail(@RequestParam("email") String email,
            @RequestParam("code") String authCode) {
        String response = mailUseCase.verifiedCode(email, authCode);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

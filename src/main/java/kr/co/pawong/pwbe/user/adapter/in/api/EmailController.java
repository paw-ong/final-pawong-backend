package kr.co.pawong.pwbe.user.adapter.in.api;

import jakarta.annotation.security.PermitAll;
import kr.co.pawong.pwbe.user.application.service.QueryUserDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/mail")
@RestController
public class EmailController {

    private final QueryUserDataService queryUserDataService;

    @PermitAll
    @GetMapping("/is-exist")
    public ResponseEntity<Boolean> isEmailExist(@RequestParam(value = "email", required = false) String email) {
        return ResponseEntity.ok(queryUserDataService.isEmailExist(email));
    }

}

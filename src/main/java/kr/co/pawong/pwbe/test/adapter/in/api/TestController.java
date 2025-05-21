package kr.co.pawong.pwbe.test.adapter.in.api;

import kr.co.pawong.pwbe.test.application.service.PublishTestMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 이벤트 발생을 위해 테스트로 만든 컨트롤러입니다.
 */
@RestController
@RequiredArgsConstructor
public class TestController {

    private final PublishTestMessageService testService;

    /**
     * 테스트여서 security 설정을 따로 하지 않기 위해 api를 "/api/adoptions"로 시작하게 했습니다.
     */
    @PostMapping("/api/adoptions/test-publish")
    public ResponseEntity<Void> publish(@RequestParam String message) {
        testService.publishTestMessage(message);
        return ResponseEntity.ok(null);
    }
}

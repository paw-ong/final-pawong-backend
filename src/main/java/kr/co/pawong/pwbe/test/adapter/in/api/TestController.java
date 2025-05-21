package kr.co.pawong.pwbe.test.adapter.in.api;

import kr.co.pawong.pwbe.test.application.service.PublishTestMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final PublishTestMessageService testService;

    @PostMapping("/api/adoptions/test-publish")
    public ResponseEntity<Void> publish(@RequestParam String message) {
        testService.publishTestMessage(message);
        return ResponseEntity.ok(null);
    }
}

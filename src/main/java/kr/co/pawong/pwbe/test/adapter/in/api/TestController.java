package kr.co.pawong.pwbe.test.adapter.in.api;

import kr.co.pawong.pwbe.global.config.KafkaTopicConfig;
import kr.co.pawong.pwbe.infrastructure.messaging.application.port.in.PublishMessageUseCase;
import kr.co.pawong.pwbe.test.adapter.in.api.dto.TestMessagePublishDto;
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

    private final PublishMessageUseCase publishTestMessageUseCase;

    /**
     * 테스트여서 security 설정을 따로 하지 않기 위해 api를 "/api/adoptions"로 시작하게 했습니다.
     * 코드를 간단하게 짜기위해 컨트롤러에서 다른 도메인의 UseCase_를 참조했습니다.
     * 실제로는 컨트롤러에서 참조할 일은 없을 것 같습니다.
     */
    @PostMapping("/api/adoptions/test-publish")
    public ResponseEntity<Void> publish(@RequestParam String name, @RequestParam Integer age) {
        publishTestMessageUseCase.publishMessage(
                KafkaTopicConfig.TEST_TOPIC, // 토픽
                new TestMessagePublishDto(name, age) // 데이터
        );
        return ResponseEntity.ok(null);
    }

}

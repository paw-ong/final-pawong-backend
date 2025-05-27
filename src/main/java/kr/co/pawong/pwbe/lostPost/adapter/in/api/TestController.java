package kr.co.pawong.pwbe.lostPost.adapter.in.api;

import kr.co.pawong.pwbe.lostPost.application.port.out.LostAnimalMessagePublishPort;
import kr.co.pawong.pwbe.lostPost.application.port.out.dto.CreatedLostAnimalPublishDto;
import kr.co.pawong.pwbe.lostPost.enums.PostType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {

    private final LostAnimalMessagePublishPort lostAnimalMessagePublishPort;

    @GetMapping("/api/adoptions/test-lost-publish")
    public void testMethod(@RequestParam(defaultValue = "1") int count) {
        for (int i = 0; i < count; i++) {
            lostAnimalMessagePublishPort.publishLostAnimalCreatedMessage(new CreatedLostAnimalPublishDto(
                    i,
                    PostType.LOST,
                    "검은 믹스견 강아지",
                    "http://openapi.animal.go.kr/openapi/service/rest/fileDownloadSrvc/files/shelter/2025/04/202505031105844.jpg"
            ));
        }
    }

    @GetMapping("/api/adoptions/test-rescue-publish")
    public void testMethod2(@RequestParam(defaultValue = "1") int count) {
        for (int i = 0; i < count; i++) {
            lostAnimalMessagePublishPort.publishLostAnimalCreatedMessage(new CreatedLostAnimalPublishDto(
                    i,
                    PostType.FOUND,
                    "검은 믹스견 강아지",
                    "http://openapi.animal.go.kr/openapi/service/rest/fileDownloadSrvc/files/shelter/2025/04/202505031105844.jpg"
            ));
        }
    }
}

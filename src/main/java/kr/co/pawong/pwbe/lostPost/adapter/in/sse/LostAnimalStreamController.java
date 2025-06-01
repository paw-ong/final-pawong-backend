package kr.co.pawong.pwbe.lostPost.adapter.in.sse;

import java.io.IOException;
import java.util.List;
import kr.co.pawong.pwbe.lostPost.application.port.in.SearchSimilarLostAnimalsUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequestMapping("/api/lost-animals")
@RequiredArgsConstructor
public class LostAnimalStreamController {

    private final SseEmitters emitters;

    private final SearchSimilarLostAnimalsUseCase searchSimilarLostAnimalsUseCase;

    @GetMapping(
            path = "/lost-posts/{lostPostId}/similar-animals/stream",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
    public SseEmitter stream(
            @PathVariable long lostPostId
    ) {
        // 유사 동물 조회
        List<LostPostCard> result = searchSimilarLostAnimalsUseCase.searchSimilarLostAnimals(
                lostPostId);
        SseEmitter emitter = emitters.create(lostPostId);
        // 유사 결과가 있으면 바로 결과 반환
        if (result != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("similar-animals")
                        .data(result));
                emitter.complete();
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }
        // 유사 결과가 null_이면 아직 인덱싱이 안된 것이기 때문에 클라이언트에서 기다리게 한다.
        return emitter;
    }
}

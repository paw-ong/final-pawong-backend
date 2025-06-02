package kr.co.pawong.pwbe.lostPost.adapter.in.sse;

import java.io.IOException;
import java.util.List;
import kr.co.pawong.pwbe.lostPost.adapter.in.sse.event.LostPostIndexedEvent;
import kr.co.pawong.pwbe.lostPost.application.port.in.SearchSimilarLostAnimalsUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.LostPostCard;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@RequiredArgsConstructor
public class LostPostIndexedEventListener {

    private final SseEmitters emitters;

    private final SearchSimilarLostAnimalsUseCase searchSimilarLostAnimalsUseCase;

    @Async
    @EventListener
    public void onIndexed(LostPostIndexedEvent event) {
        Long postId = event.getLostPostId();
        List<LostPostCard> result = searchSimilarLostAnimalsUseCase.searchSimilarLostAnimals(postId);

        emitters.getAll(postId).forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("similar-animals")
                        .data(result));
                emitter.complete();
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });
    }
}

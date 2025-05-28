package kr.co.pawong.pwbe.lostPost.adapter.in.sse;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@RequiredArgsConstructor
public class SseEmitters {

    /**
     * 각 postId(Long)마다 emitter_들을 보관
     */
    private final Map<Long, CopyOnWriteArrayList<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter create(Long postId) {
        SseEmitter emitter = new SseEmitter(0L); // 타임아웃 없음
        emitters
                .computeIfAbsent(postId, id -> new CopyOnWriteArrayList<>())
                .add(emitter);

        // 완료·타임아웃·에러 시 cleanup
        emitter.onCompletion(() -> remove(postId, emitter));
        emitter.onTimeout  (() -> remove(postId, emitter));
        emitter.onError    ((e) -> remove(postId, emitter));

        return emitter;
    }

    private void remove(Long postId, SseEmitter emitter) {
        List<SseEmitter> list = emitters.get(postId);
        if (list != null) {
            list.remove(emitter);
            if (list.isEmpty()) {
                emitters.remove(postId);
            }
        }
    }

    public List<SseEmitter> getAll(Long postId) {
        return emitters.getOrDefault(postId, new CopyOnWriteArrayList<>());
    }
}

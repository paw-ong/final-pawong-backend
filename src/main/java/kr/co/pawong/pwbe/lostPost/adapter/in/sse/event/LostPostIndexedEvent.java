package kr.co.pawong.pwbe.lostPost.adapter.in.sse.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class LostPostIndexedEvent extends ApplicationEvent {

    private final Long lostPostId;

    public LostPostIndexedEvent(Object source, Long lostPostId) {
        super(source);
        this.lostPostId = lostPostId;
    }
}

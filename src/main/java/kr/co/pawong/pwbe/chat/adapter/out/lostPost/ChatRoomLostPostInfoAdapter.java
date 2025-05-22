package kr.co.pawong.pwbe.chat.adapter.out.lostPost;

import kr.co.pawong.pwbe.chat.application.port.out.ChatRoomLostPostInfoPort;
import kr.co.pawong.pwbe.lostPost.application.port.in.QueryLostPostDataUseCase;
import kr.co.pawong.pwbe.chat.adapter.out.lostPost.dto.ChatRoomLostPostInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatRoomLostPostInfoAdapter implements ChatRoomLostPostInfoPort {

    private final QueryLostPostDataUseCase queryLostPostDataUseCase;

    @Override
    public ChatRoomLostPostInfo getLostPostInfosById(Long postId) {
        return queryLostPostDataUseCase.findChatRoomLostPostInfosById(postId);
    }
}

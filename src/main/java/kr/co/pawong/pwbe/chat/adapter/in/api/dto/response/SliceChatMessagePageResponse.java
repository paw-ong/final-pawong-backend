package kr.co.pawong.pwbe.chat.adapter.in.api.dto.response;

import java.util.List;
import kr.co.pawong.pwbe.chat.application.port.in.dto.ChatMessageDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SliceChatMessagePageResponse {
    private List<ChatMessageDetail> messages;
    private boolean hasNext;
    private int page;
    private int size;

}

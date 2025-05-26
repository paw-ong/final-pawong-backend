package kr.co.pawong.pwbe.chat.adapter.in.api.dto.response;

import java.util.List;
import kr.co.pawong.pwbe.chat.application.port.in.dto.ChatMessageDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatMessagesResponse {

    List<ChatMessageDetail> chatMessageDetails;
}

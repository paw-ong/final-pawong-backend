package kr.co.pawong.pwbe.chat.application.port.in;

import java.util.List;
import kr.co.pawong.pwbe.chat.adapter.in.api.dto.response.SliceChatMessagePageResponse;
import kr.co.pawong.pwbe.chat.application.port.in.dto.ChatMessageDetail;
import org.springframework.data.domain.Pageable;

public interface QueryChatMessageDataUseCase {

    List<ChatMessageDetail> findAllMessagesInChatRoom(Long userId, Long chatRoomId);

    SliceChatMessagePageResponse getSliceMessage(Long userId, Long roomId, Pageable pageable);
}

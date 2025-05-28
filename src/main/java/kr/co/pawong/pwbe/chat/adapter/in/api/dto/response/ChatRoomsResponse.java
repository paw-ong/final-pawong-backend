package kr.co.pawong.pwbe.chat.adapter.in.api.dto.response;

import java.util.List;
import kr.co.pawong.pwbe.chat.application.port.in.dto.ChatRoomDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomsResponse {

    private List<ChatRoomDetail> chatRoomsDetails;

}

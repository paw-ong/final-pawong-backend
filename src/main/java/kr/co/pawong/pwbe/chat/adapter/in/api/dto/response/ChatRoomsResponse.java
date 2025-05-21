package kr.co.pawong.pwbe.chat.adapter.in.api.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomsResponse {

    private List<ChatRoomDetail> chatRoomsDetails;

}

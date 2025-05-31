package kr.co.pawong.pwbe.chat.adapter.in.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 유저가 채팅방에 존재하는지 나타내는 dto
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomContainsUserResponse {

    private boolean exists;
}

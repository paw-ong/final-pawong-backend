package kr.co.pawong.pwbe.chat.application.service;

import java.util.ArrayList;
import java.util.List;
import kr.co.pawong.pwbe.chat.adapter.in.api.dto.response.ChatRoomDetail;
import kr.co.pawong.pwbe.chat.application.port.in.QueryChatRoomDataUseCase;
import kr.co.pawong.pwbe.chat.application.port.out.ChatRoomDataQueryPort;
import kr.co.pawong.pwbe.chat.application.port.out.ChatRoomLostPostInfoPort;
import kr.co.pawong.pwbe.chat.domain.ChatRoom;
import kr.co.pawong.pwbe.lostPost.application.port.in.dto.ChatRoomLostPostInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueryChatRoomDataService implements QueryChatRoomDataUseCase {

    private final ChatRoomLostPostInfoPort lostPostInfoPort;
    private final ChatRoomDataQueryPort chatRoomDataQueryPort;

    // 유저가 속한 모든 채팅방을 조회해서 ChatRoomDetail 리스트로 반환
    @Override
    public List<ChatRoomDetail> findAllChatRooms(Long userId) {
        List<ChatRoom> chatRooms = chatRoomDataQueryPort.findChatRoomsByUserId(userId);
        List<ChatRoomDetail> chatRoomDetails = new ArrayList<>(chatRooms.size());
        for (ChatRoom chatRoom : chatRooms) {
            Long postId = chatRoom.getPostId();
            ChatRoomLostPostInfo lostPostInfo = lostPostInfoPort.getLostPostInfosById(postId);
            chatRoomDetails.add(new ChatRoomDetail(lostPostInfo, chatRoom.getChatRoomId()));
        }

        return chatRoomDetails;
    }
}

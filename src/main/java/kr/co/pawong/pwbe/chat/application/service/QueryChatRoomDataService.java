package kr.co.pawong.pwbe.chat.application.service;

import java.util.ArrayList;
import java.util.List;
import kr.co.pawong.pwbe.chat.adapter.out.lostPost.dto.ChatRoomLostPostAuthorInfo;
import kr.co.pawong.pwbe.chat.adapter.out.lostPost.dto.ChatRoomLostPostInfo;
import kr.co.pawong.pwbe.chat.application.port.in.QueryChatRoomDataUseCase;
import kr.co.pawong.pwbe.chat.application.port.in.dto.ChatRoomDetail;
import kr.co.pawong.pwbe.chat.application.port.out.ChatRoomDataQueryPort;
import kr.co.pawong.pwbe.chat.application.port.out.ChatRoomLostPostInfoPort;
import kr.co.pawong.pwbe.chat.application.port.out.ChatRoomUserInfoPort;
import kr.co.pawong.pwbe.chat.domain.ChatRoom;
import kr.co.pawong.pwbe.chat.enums.ChatRoomStatus;
import kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueryChatRoomDataService implements QueryChatRoomDataUseCase {

    private final ChatRoomLostPostInfoPort lostPostInfoPort;
    private final ChatRoomDataQueryPort chatRoomDataQueryPort;
    private final ChatRoomUserInfoPort chatRoomUserInfoPort;

    // 유저가 속한 모든 채팅방을 조회해서 ChatRoomDetail 리스트로 반환
    @Override
    public List<ChatRoomDetail> findUserChatRooms(Long userId) {
        List<ChatRoom> chatRooms = chatRoomDataQueryPort.findChatRoomsByUserId(userId);
        List<ChatRoomDetail> chatRoomDetails = new ArrayList<>(chatRooms.size());

        for (ChatRoom chatRoom : chatRooms) {
            Long postId = chatRoom.getPostId();
            String participantUserName = chatRoomUserInfoPort.getUserNameById(
                    chatRoom.getParticipantId()).userName();
            ChatRoomLostPostInfo lostPostInfo = lostPostInfoPort.getLostPostInfosById(postId);
            chatRoomDetails.add(new ChatRoomDetail(lostPostInfo, chatRoom.getChatRoomId(),
                    chatRoom.getParticipantId(), participantUserName,
                    chatRoom.getStatus()));
        }

        return chatRoomDetails;
    }

    // 실종 공고에 속한 모든 채팅방들을 조회해서 ChatRoomDetail 리스트로 반환
    @Override
    public List<ChatRoomDetail> findUserChatRoomsByPostId(Long userId, Long postId) {
        ChatRoomLostPostAuthorInfo lostPostAuthorInfo = lostPostInfoPort.getLostPostAuthorInfoById(
                postId);
        // 메서드를 호출한 사용자가 해당 공고의 author가 아닌 경우에는 해당 채팅방 목록을 조회할 권한이 없다.
        if (!lostPostAuthorInfo.authorId().equals(userId)) {
            throw new BaseException(CustomErrorCode.FORBIDDEN_CHATROOMS_VIEW);
        }
        List<ChatRoom> chatRooms = chatRoomDataQueryPort.findChatRoomsByPostId(postId);
        List<ChatRoomDetail> chatRoomDetails = new ArrayList<>(chatRooms.size());

        for (ChatRoom chatRoom : chatRooms) {
            ChatRoomLostPostInfo lostPostInfo = lostPostInfoPort.getLostPostInfosById(postId);
            String participantUserName = chatRoomUserInfoPort.getUserNameById(
                    chatRoom.getParticipantId()).userName();
            chatRoomDetails.add(new ChatRoomDetail(lostPostInfo, chatRoom.getChatRoomId(),
                    chatRoom.getParticipantId(), participantUserName,
                    chatRoom.getStatus()));
        }
        return chatRoomDetails;
    }

    @Override
    public boolean userExistsInChatRoom(Long userId, Long chatRoomId) {
        ChatRoom chatRoom = chatRoomDataQueryPort.findChatRoomByIdOrThrow(chatRoomId);
        return chatRoom.getParticipantId().equals(userId) || chatRoom.getAuthorId().equals(userId);
    }

    @Override
    public boolean chatRoomIsActive(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomDataQueryPort.findChatRoomByIdOrThrow(chatRoomId);
        return (chatRoom.getStatus() == ChatRoomStatus.ACTIVE);
    }
}

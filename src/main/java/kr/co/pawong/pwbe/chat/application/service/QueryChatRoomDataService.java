package kr.co.pawong.pwbe.chat.application.service;

import java.util.ArrayList;
import java.util.List;
import kr.co.pawong.pwbe.chat.adapter.out.lostPost.dto.ChatRoomLostPostAuthorInfo;
import kr.co.pawong.pwbe.chat.adapter.out.lostPost.dto.ChatRoomLostPostInfo;
import kr.co.pawong.pwbe.chat.application.port.in.QueryChatMessageDataUseCase;
import kr.co.pawong.pwbe.chat.application.port.in.QueryChatRoomDataUseCase;
import kr.co.pawong.pwbe.chat.application.port.in.dto.ChatRoomDetail;
import kr.co.pawong.pwbe.chat.application.port.out.ChatMessageDataQueryPort;
import kr.co.pawong.pwbe.chat.application.port.out.ChatRoomDataQueryPort;
import kr.co.pawong.pwbe.chat.application.port.out.ChatRoomLostPostInfoPort;
import kr.co.pawong.pwbe.chat.application.port.out.ChatRoomUserInfoPort;
import kr.co.pawong.pwbe.chat.domain.ChatMessage;
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
    private final ChatMessageDataQueryPort chatMessageDataQueryPort;

    // 유저가 속한 모든 채팅방을 조회해서 ChatRoomDetail 리스트로 반환
    @Override
    public List<ChatRoomDetail> findUserChatRooms(Long userId) {
        List<ChatRoom> chatRooms = chatRoomDataQueryPort.findChatRoomsByUserId(userId);
        List<ChatRoomDetail> chatRoomDetails = new ArrayList<>(chatRooms.size());

        for (ChatRoom chatRoom : chatRooms) {
            // 데이터 준비
            Long postId = chatRoom.getPostId();
            Long chatRoomId = chatRoom.getChatRoomId();
            String participantUserName = chatRoomUserInfoPort.getUserNameById(
                    chatRoom.getParticipantId()).userName();
            String latestMessageContent = findLatestChatMessageContent(chatRoomId);
            ChatRoomLostPostInfo lostPostInfo = lostPostInfoPort.getLostPostInfosById(postId);

            // 리스트에 데이터 추가
            chatRoomDetails.add(new ChatRoomDetail(lostPostInfo, chatRoomId,
                    chatRoom.getParticipantId(), participantUserName,
                    chatRoom.getStatus(), latestMessageContent));
        }
        return chatRoomDetails;
    }

    // 실종 공고에 속한 모든 채팅방들을 조회해서 ChatRoomDetail 리스트로 반환
    @Override
    public List<ChatRoomDetail> findUserChatRoomsByPostId(Long userId, Long postId) {
        // 실종 공고의 작성자 닉네임을 받아온다.
        ChatRoomLostPostAuthorInfo lostPostAuthorInfo = lostPostInfoPort.getLostPostAuthorInfoById(
                postId);
        // 메서드를 호출한 사용자가 해당 공고의 author가 아닌 경우에는 해당 채팅방 목록을 조회할 권한이 없다.
        if (!lostPostAuthorInfo.authorId().equals(userId)) {
            throw new BaseException(CustomErrorCode.FORBIDDEN_CHATROOMS_ACCESS);
        }
        // ChatRooms 받아오고, 채팅방 목록으로 반환할 ChatRoomDetail 리스트 준비
        List<ChatRoom> chatRooms = chatRoomDataQueryPort.findChatRoomsByPostId(postId);
        List<ChatRoomDetail> chatRoomDetails = new ArrayList<>(chatRooms.size());

        // 공고가 이미 정해져 있으므로 미리 ChatRoomLostPostInfo를 받아오기
        ChatRoomLostPostInfo lostPostInfo = lostPostInfoPort.getLostPostInfosById(postId);

        for (ChatRoom chatRoom : chatRooms) {
            String participantUserName = chatRoomUserInfoPort.getUserNameById(
                    chatRoom.getParticipantId()).userName();
            String latestMessageContent = findLatestChatMessageContent(
                    chatRoom.getChatRoomId());

            chatRoomDetails.add(new ChatRoomDetail(lostPostInfo, chatRoom.getChatRoomId(),
                    chatRoom.getParticipantId(), participantUserName,
                    chatRoom.getStatus(), latestMessageContent));
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

    // 채팅방 내 마지막으로 전송된 메시지를 받아오거나, 메시지가 없다면 빈 문자열
    private String findLatestChatMessageContent(Long chatRoomId) {
        try {
            ChatMessage latestChatMessage = chatMessageDataQueryPort.findLatestChatMessageInChatRoomOrThrow(
                    chatRoomId);
            return latestChatMessage.getContent();
        } catch (BaseException e) {
            return "";
        }
    }
}

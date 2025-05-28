package kr.co.pawong.pwbe.chat.application.service;

import java.util.List;
import kr.co.pawong.pwbe.chat.adapter.out.lostPost.dto.ChatRoomLostPostInfo;
import kr.co.pawong.pwbe.chat.application.port.in.QueryChatRoomDataUseCase;
import kr.co.pawong.pwbe.chat.application.port.in.dto.ChatRoomDetail;
import kr.co.pawong.pwbe.chat.application.port.out.ChatMessageDataQueryPort;
import kr.co.pawong.pwbe.chat.application.port.out.ChatRoomDataQueryPort;
import kr.co.pawong.pwbe.chat.application.port.out.ChatRoomLostPostInfoPort;
import kr.co.pawong.pwbe.chat.application.port.out.ChatRoomUserInfoPort;
import kr.co.pawong.pwbe.chat.domain.ChatMessage;
import kr.co.pawong.pwbe.chat.domain.ChatRoom;
import kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryChatRoomDataService implements QueryChatRoomDataUseCase {

    private final ChatRoomLostPostInfoPort chatRoomLostPostInfoPort;
    private final ChatRoomDataQueryPort chatRoomDataQueryPort;
    private final ChatRoomUserInfoPort chatRoomUserInfoPort;
    private final ChatMessageDataQueryPort chatMessageDataQueryPort;

    // 유저의 모든 채팅방들을 ChatRoomDetail로 반환
    @Override
    public List<ChatRoomDetail> findUserChatRooms(Long userId) {
        List<ChatRoom> rooms = chatRoomDataQueryPort.findChatRoomsByUserId(userId);
        return buildUserChatRoomDetails(rooms);
    }

    // 공고로 연결된 채팅방들을 ChatRoomDetail 리스트로 반환
    @Override
    public List<ChatRoomDetail> findUserChatRoomsByPostId(Long userId, Long postId) {
        Long authorId =
                chatRoomLostPostInfoPort.getLostPostInfosById(postId).authorId();
        // 요청 유저가 작성자가 아닌 경우 권한 없음
        if (!authorId.equals(userId)) {
            throw new BaseException(CustomErrorCode.FORBIDDEN_CHATROOMS_ACCESS);
        }

        List<ChatRoom> rooms = chatRoomDataQueryPort.findChatRoomsByPostId(postId);
        // 특정 공고로 연결돤 채팅방이므로, 공통의 ChatRoomLostPostInfo 활용
        ChatRoomLostPostInfo commonInfo = chatRoomLostPostInfoPort.getLostPostInfosById(postId);
        return buildPostChatRoomDetails(rooms, commonInfo);
    }

    @Override
    public boolean isUserInChatRoom(Long userId, Long chatRoomId) {
        ChatRoom room = chatRoomDataQueryPort.findChatRoomByIdOrThrow(chatRoomId);
        return room.userExistsInChatRoom(userId);
    }

    @Override
    public boolean isChatRoomActive(Long chatRoomId) {
        ChatRoom room = chatRoomDataQueryPort.findChatRoomByIdOrThrow(chatRoomId);
        return room.isActive();
    }

    // 유저별 각 방마다 lostPostInfo를 조회해서 매핑
    private List<ChatRoomDetail> buildUserChatRoomDetails(List<ChatRoom> rooms) {
        return rooms.stream()
                .map(room -> {
                    ChatRoomLostPostInfo postInfo =
                            chatRoomLostPostInfoPort.getLostPostInfosById(room.getPostId());
                    return toDetail(room, postInfo);
                })
                .toList();
    }

    // 특정 공고로 연결된 채팅방들은 공통 ChatRoomLostPostInfo를 가지므로 param으로 가짐
    private List<ChatRoomDetail> buildPostChatRoomDetails(
            List<ChatRoom> rooms,
            ChatRoomLostPostInfo commonInfo
    ) {
        return rooms.stream()
                .map(room -> toDetail(room, commonInfo))
                .toList();
    }

    // ChatRoom을 ChatRoomDetail으로 변환하는 공통 로직
    private ChatRoomDetail toDetail(ChatRoom room, ChatRoomLostPostInfo postInfo) {
        String participantName = chatRoomUserInfoPort
                .getUserNameById(room.getParticipantId())
                .userName();
        String latestMessageContent = fetchLatestMessageContentOrEmpty(room.getChatRoomId());

        return new ChatRoomDetail(
                postInfo,
                room.getChatRoomId(),
                room.getParticipantId(),
                participantName,
                room.getStatus(),
                latestMessageContent
        );
    }

    // 채팅방 내 가장 최근 메시지 반환. 메시지가 없다면 빈 문자열 반환
    private String fetchLatestMessageContentOrEmpty(Long chatRoomId) {
        try {
            ChatMessage latest = chatMessageDataQueryPort
                    .findLatestChatMessageInChatRoomOrThrow(chatRoomId);
            return latest.getContent();
        } catch (BaseException e) {
            return "";
        }
    }
}

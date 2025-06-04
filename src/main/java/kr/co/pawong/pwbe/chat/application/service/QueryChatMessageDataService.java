package kr.co.pawong.pwbe.chat.application.service;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kr.co.pawong.pwbe.chat.adapter.in.api.dto.response.SliceChatMessagePageResponse;
import kr.co.pawong.pwbe.chat.adapter.out.persistence.jpa.entity.ChatMessageEntity;
import kr.co.pawong.pwbe.chat.application.port.in.QueryChatMessageDataUseCase;
import kr.co.pawong.pwbe.chat.application.port.in.QueryChatRoomDataUseCase;
import kr.co.pawong.pwbe.chat.application.port.in.dto.ChatMessageDetail;
import kr.co.pawong.pwbe.chat.application.port.out.ChatMessageCachePort;
import kr.co.pawong.pwbe.chat.application.port.out.ChatMessageDataQueryPort;
import kr.co.pawong.pwbe.chat.domain.ChatMessage;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import kr.co.pawong.pwbe.user.application.port.out.UserDataQueryPort;
import kr.co.pawong.pwbe.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueryChatMessageDataService implements QueryChatMessageDataUseCase {

    private final QueryChatRoomDataUseCase queryChatRoomDataUseCase;
    private final ChatMessageDataQueryPort chatMessageDataQueryPort;
    private final UserDataQueryPort userDataQueryPort;
    private final ChatMessageCachePort chatMessageCachePort;

    @Override
    public List<ChatMessageDetail> findAllMessagesInChatRoom(Long userId, Long chatRoomId) {
        // 유저가 해당 채팅방에 속하지 않다면 예외를 발생
        if (!queryChatRoomDataUseCase.isUserInChatRoom(userId, chatRoomId)) {
            throw new BaseException(FORBIDDEN_CHATMESSAGE_QUERY);
        }

        List<ChatMessage> chatMessages = chatMessageDataQueryPort
                .findChatMessagesByChatRoomIdInLatestOrder(chatRoomId);

        return chatMessages.stream()
                .map(msg -> {
                            User sender = userDataQueryPort.findByUserIdOrThrow(msg.getSenderId());
                            return ChatMessageDetail
                                    .from(msg)
                                    .updateSenderInfo(sender.getNickname(), sender.getProfileImage());
                        }
                )
                .collect(Collectors.toList());
    }

    /**
     * page number에 따라서 메세지를 슬라이싱하여 반환한다
     * 첫 페이지 로드일 때에는 findLatestMessagesInChatRoom
     * 이후 페이지 로드 때에는 findOlderMessagesInChatRoom
     * @param userId
     * @param roomId
     * @param pageable
     * @return
     */
    @Override
    public SliceChatMessagePageResponse getSliceMessage(Long userId, Long roomId, Pageable pageable) {
        if (!queryChatRoomDataUseCase.isUserInChatRoom(userId, roomId)) {
            throw new BaseException(FORBIDDEN_CHATMESSAGE_QUERY);
        }

        if(pageable.getPageNumber() == 0)
            return findLatestMessagesInChatRoom(roomId, pageable);

        return findOlderMessagesInChatRoom(roomId, pageable);
    }

    /**
     * 최근 메세지를 반환한다
     * 처음으로 캐시에서 데이터를 가져오는 경우 전체 데이터의 개수를 계산하여 HasNext를 반환한다
     * 만약 캐시의 데이터가 page size 만큼 존재하지 않으면 DB에서 데이터를 가져온 뒤에 캐시에 저장한다
     * @param roomId
     * @param pageable
     * @return SliceChatMessagePageResponse
     */
    private SliceChatMessagePageResponse findLatestMessagesInChatRoom(Long roomId, Pageable pageable) {

        /* get messages from cache */
        List<ChatMessage> recentChatMessage = chatMessageCachePort.getLatestMessages(roomId);
        Long totalCount = chatMessageCachePort.getTotalCount(roomId);
        if(totalCount==0)  updateCacheTotalCount(roomId);
        boolean hasNext = totalCount > recentChatMessage.size();

        /* get messages from a database */
        if(recentChatMessage.size() < pageable.getPageSize()) {
            Slice<ChatMessage> sliceMessages = chatMessageDataQueryPort.findSliceMessages(roomId, PageRequest.of(0, pageable.getPageSize()));
            List<ChatMessage> chatMessageList = new ArrayList<>(sliceMessages.getContent());
            Collections.reverse(chatMessageList);
            hasNext = sliceMessages.hasNext();

            /* save to cache */
            recentChatMessage = chatMessageList;
            chatMessageCachePort.cacheMessageChunk(chatMessageList, roomId);
            updateCacheTotalCount(roomId);
        }

        List<ChatMessageDetail> chatMessageDetails = recentChatMessage.stream().map(this::toDetailWithSender).toList();
        return new SliceChatMessagePageResponse(chatMessageDetails, hasNext, 0, chatMessageDetails.size());
    }

    private void updateCacheTotalCount(Long roomId) {
        Long count = chatMessageDataQueryPort.countByChatRoomId(roomId);
        chatMessageCachePort.updateTotalCount(roomId, count);
    }

    /**
     * 이전 메세지들은 데이터베이스에서 조회하여 반환한다.
     * @param roomId
     * @param pageable
     * @return SliceChatMessagePageResponse
     */
    private SliceChatMessagePageResponse findOlderMessagesInChatRoom(Long roomId, Pageable pageable) {
        Slice<ChatMessage> sliceMessages = chatMessageDataQueryPort.findSliceMessages(roomId, pageable);
        List<ChatMessage> chatMessageList = new ArrayList<>(sliceMessages.getContent());
        Collections.reverse(chatMessageList);
        boolean hasNext = sliceMessages.hasNext();

        List<ChatMessageDetail> chatMessageDetails = chatMessageList.stream().map(this::toDetailWithSender).toList();
        return new SliceChatMessagePageResponse(chatMessageDetails, hasNext, pageable.getPageNumber(), chatMessageDetails.size());
    }

    // TODO: ChatMessage 마다 사용자 정보를 가져오는 로직 리팩토링
    private ChatMessageDetail toDetailWithSender(ChatMessage msg) {
        User sender = userDataQueryPort.findByUserIdOrThrow(msg.getSenderId());
        return ChatMessageDetail
                .from(msg)
                .updateSenderInfo(sender.getNickname(), sender.getProfileImage());
    }
}

package kr.co.pawong.pwbe.chat.application.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import kr.co.pawong.pwbe.chat.application.port.in.QueryChatMessageDataUseCase;
import kr.co.pawong.pwbe.chat.application.port.in.QueryChatRoomDataUseCase;
import kr.co.pawong.pwbe.chat.application.port.in.dto.ChatMessageDetail;
import kr.co.pawong.pwbe.chat.application.port.out.ChatMessageCachePort;
import kr.co.pawong.pwbe.chat.application.port.out.ChatMessageDataQueryPort;
import kr.co.pawong.pwbe.chat.domain.ChatMessage;
import kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import kr.co.pawong.pwbe.user.application.port.out.UserDataQueryPort;
import kr.co.pawong.pwbe.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueryChatMessageDataService implements QueryChatMessageDataUseCase {

    private static final int CACHE_PAGE_SIZE = 50;

    private final QueryChatRoomDataUseCase queryChatRoomDataUseCase;
    private final ChatMessageDataQueryPort chatMessageDataQueryPort;
    private final UserDataQueryPort userDataQueryPort;
    private final ChatMessageCachePort chatMessageCachePort;

    @Override
    public List<ChatMessageDetail> findAllMessagesInChatRoom(Long userId, Long chatRoomId) {
        // 유저가 해당 채팅방에 속하지 않다면 예외를 발생
        if (!queryChatRoomDataUseCase.isUserInChatRoom(userId, chatRoomId)) {
            throw new BaseException(CustomErrorCode.FORBIDDEN_CHATMESSAGE_QUERY);
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
     * “beforeMillis == null” → 가장 최신 페이지 용 호출
     * “beforeMillis != null” → 과거 페이지 용 호출
     * @param userId
     * @param roomId
     * @param beforeMillis
     * @return
     */
    @Override
    public List<ChatMessageDetail> findLatestMessagesInChatRoom(Long userId, Long roomId, Long beforeMillis) {
        if (!queryChatRoomDataUseCase.isUserInChatRoom(userId, roomId)) {
            throw new BaseException(CustomErrorCode.FORBIDDEN_CHATMESSAGE_QUERY);
        }
        if (beforeMillis == null) return fetchLatestPage(roomId);
        return fetchOlderPage(roomId, beforeMillis);
    }

    /**
     * 채팅방의 최신 N건의 메세지를 반환
     * @param roomId
     * @return
     */
    private List<ChatMessageDetail> fetchLatestPage(Long roomId) {
        // 1) Redis 에서 최신 PAGE_SIZE건 조회
        List<ChatMessage> cached = chatMessageCachePort.getLatestMessages(roomId, CACHE_PAGE_SIZE);

        if (cached.size() == CACHE_PAGE_SIZE) {
            return cached.stream()
                    .map(this::toDetailWithSender)
                    .collect(Collectors.toList());
        }

        // 2) Redis 데이터가 없으면
        if(cached.isEmpty()) {
            List<ChatMessage> fromDb = chatMessageDataQueryPort.findLatestNByChatRoom(roomId, CACHE_PAGE_SIZE);
            Collections.reverse(fromDb);
            return fromDb.stream()
                    .map(this::toDetailWithSender)
                    .collect(Collectors.toList());
        }

        // 3) Redis에 충분한 데이터가 없으면, DB에서 추가 조회하여 반환
        int need = CACHE_PAGE_SIZE-cached.size();
        Instant oldestRedisCreatedAt = cached.getFirst().getCreatedAt();
        List<ChatMessage> dbList = chatMessageDataQueryPort
                .findByChatRoomIdAndCreatedAtBeforeOrderByCreatedAtDesc(
                        roomId,
                        oldestRedisCreatedAt,
                        need
                );
        Collections.reverse(dbList);

        // 4) 최종 반환 리스트: (DB 쪽 과거 메시지들) + (Redis 쪽 최신 메시지들)
        List<ChatMessageDetail> result = new ArrayList<>();
        dbList.forEach(msg -> result.add(toDetailWithSender(msg)));
        cached.forEach(msg -> result.add(toDetailWithSender(msg)));
        return result;
    }

    /**
     * Redis에 beforeMillis 이전 메시지가 충분히 없다면 DB에서 보충 조회한 다음 오름차순으로 합쳐서 반환
     * @param roomId
     * @param beforeMillis
     * @return
     */
    private List<ChatMessageDetail> fetchOlderPage(Long roomId, Long beforeMillis) {
        // 1) DB에서 “createdAt < beforeInstant”, 내림차순으로 최대 N건 가져오기
        Instant beforeInstant = Instant.ofEpochMilli(beforeMillis);
        List<ChatMessage> dbList = chatMessageDataQueryPort
                .findByChatRoomIdAndCreatedAtBeforeOrderByCreatedAtDesc(
                        roomId,
                        beforeInstant,
                        CACHE_PAGE_SIZE
                );

        // 2) “과거→최신” 순서로 뒤집기
        Collections.reverse(dbList);

        // 3) DTO 변환 + 보낸 사람 정보 채우기
        return dbList.stream()
                .map(this::toDetailWithSender)
                .collect(Collectors.toList());
    }

    // ChatMessage → ChatMessageDetail + senderName, senderProfileImage 채워서 반환
    private ChatMessageDetail toDetailWithSender(ChatMessage msg) {
        User sender = userDataQueryPort.findByUserIdOrThrow(msg.getSenderId());
        return ChatMessageDetail
                .from(msg)
                .updateSenderInfo(sender.getNickname(), sender.getProfileImage());
    }
}

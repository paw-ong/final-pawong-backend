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

    @Override
    public List<ChatMessageDetail> findLastestMessagesInChatRoom(Long userId, Long roomId) {

        // 1) Redis 에서 최신 PAGE_SIZE건 조회
        List<ChatMessageDetail> latestMessages =
                chatMessageCachePort.getLatestMessages(roomId, CACHE_PAGE_SIZE)
                        .stream()
                        .map(msg -> {
                                    User sender = userDataQueryPort.findByUserIdOrThrow(msg.getSenderId());
                                    return ChatMessageDetail
                                            .from(msg)
                                            .updateSenderInfo(sender.getNickname(), sender.getProfileImage());
                                }
                        )
                        .toList();
        if (latestMessages.size() == CACHE_PAGE_SIZE)
            return latestMessages;

        // 2) Redis 데이터가 없으면
        if(latestMessages.isEmpty()) {
            List<ChatMessage> fromDb = chatMessageDataQueryPort.findLatestNByChatRoom(roomId, CACHE_PAGE_SIZE);
            Collections.reverse(fromDb);
            return fromDb.stream()
                    .map(msg -> {
                        User sender = userDataQueryPort.findByUserIdOrThrow(msg.getSenderId());
                        return ChatMessageDetail
                                .from(msg)
                                .updateSenderInfo(sender.getNickname(), sender.getProfileImage());
                    })
                    .collect(Collectors.toList());
        }

        // 2) Redis에 충분한 데이터가 없으면, DB에서 추가 조회하여 반환
        int need = CACHE_PAGE_SIZE-latestMessages.size();
        Instant oldestRedisCreatedAt = Instant.ofEpochMilli(latestMessages.getFirst().getCreatedAt());
        List<ChatMessage> dbList = chatMessageDataQueryPort
                .findByChatRoomIdAndCreatedAtBeforeOrderByCreatedAtDesc(
                        roomId,
                        oldestRedisCreatedAt,
                        need
                );

        // 3) DB 엔티티 → DTO로 변환 후, "역순(시간 오름차순)"으로 맞춘 다음 Redis 리스트 뒤에 붙임
        List<ChatMessageDetail> dbDtoList = dbList.stream()
                .map(msg -> {
                    User sender = userDataQueryPort.findByUserIdOrThrow(msg.getSenderId());
                    return ChatMessageDetail
                            .from(msg)
                            .updateSenderInfo(sender.getNickname(), sender.getProfileImage());
                })
                .sorted(Comparator.comparingLong(ChatMessageDetail::getCreatedAt))
                .toList();
        Collections.reverse(dbList);

        // 4) 최종 반환 리스트: (DB 쪽 과거 메시지들) + (Redis 쪽 최신 메시지들)
        List<ChatMessageDetail> result = new ArrayList<>();
        result.addAll(dbDtoList);
        result.addAll(latestMessages);
        return result;
    }
}

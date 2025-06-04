package kr.co.pawong.pwbe.chat.adapter.out.cache.redis;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.CHATMESSAGE_NOT_FOUND;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kr.co.pawong.pwbe.chat.application.port.out.ChatMessageCachePort;
import kr.co.pawong.pwbe.chat.domain.ChatMessage;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisChatMessageAdapter implements ChatMessageCachePort {

    private static final int MAX_CACHE_SIZE = 50;
    private static final String CHAT_ROOM_PREFIX = "chat:room:";
    private static final String MESSAGE_SUFFIX = ":messages";
    private static final String TOTAL_COUNT_SUFFIX = ":total:count";

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Redis에 채팅 메시지를 저장하고, 최대 n개만 보관되도록 trim 처리한다.
     *
     * @param chatRoomId  캐시할 채팅방 ID
     * @param chatMessage 저장할 ChatMessageDto 객체
     */
    @Override
    public void cacheMessage(Long chatRoomId, ChatMessage chatMessage) {
        String key = getRedisKey(CHAT_ROOM_PREFIX + chatRoomId, MESSAGE_SUFFIX);
        String json = convertToJson(chatMessage);

        redisTemplate.opsForList().leftPush(key, json);
        redisTemplate.opsForList().trim(key, 0, MAX_CACHE_SIZE - 1);
        countMessages(chatRoomId);
    }

    /**
     * Redis에 채팅 메시지 리스트를 저장하고, 최대 n개만 보관되도록 trim 처리한다.
     *
     * @param chatMessageList
     * @param chatRoomId
     */
    @Override
    public void cacheMessageChunk(List<ChatMessage> chatMessageList, Long chatRoomId) {
        String key = getRedisKey(CHAT_ROOM_PREFIX + chatRoomId, MESSAGE_SUFFIX);
        List<String> jsonList = chatMessageList.stream().map(this::convertToJson).toList();

        for (String json : jsonList) {
            redisTemplate.opsForList().leftPush(key, json);
            countMessages(chatRoomId);
        }
        redisTemplate.opsForList().trim(key, 0, MAX_CACHE_SIZE - 1);
    }

    /**
     * Redis에서 해당 채팅룸의 최신 N건 메시지를 조회하여 DTO 리스트로 반환한다.
     *
     * @param chatRoomId 조회할 채팅방 ID
     * @return 최신 N건의 ChatMessage 리스트 (없으면 빈 리스트)
     */
    public List<ChatMessage> getLatestMessages(Long chatRoomId) {
        String key = getRedisKey(CHAT_ROOM_PREFIX + chatRoomId, MESSAGE_SUFFIX);
        List<String> messageList = redisTemplate.opsForList().range(key, 0, MAX_CACHE_SIZE -1);

        if (messageList == null || messageList.isEmpty()) {
            return new ArrayList<>();
        }

        Collections.reverse(messageList);
        return messageList.stream()
                .map(this::convertFromJson)
                .toList();
    }


    @Override
    public Long getTotalCount(Long roomId) {
        String key = getRedisKey(CHAT_ROOM_PREFIX + roomId, TOTAL_COUNT_SUFFIX);
        String count = redisTemplate.opsForValue().get(key);
        if (count == null)  return 0L;

        try {
            return Long.parseLong(count);
        } catch (NumberFormatException exception) {
            return 0L;
        }
    }

    @Override
    public void updateTotalCount(Long chatRoomId, Long count) {
        String countKey = getRedisKey(CHAT_ROOM_PREFIX + chatRoomId, TOTAL_COUNT_SUFFIX);

        redisTemplate.opsForValue().set(countKey, String.valueOf(count));
    }


    /**
     * 채팅방에 메시지가 하나 저장될 때마다 Redis에 저장된 totalCount를 1 증가시킨다.
     *
     * @param chatRoomId
     */
    private void countMessages(Long chatRoomId) {
        String countKey = getRedisKey(CHAT_ROOM_PREFIX + chatRoomId, TOTAL_COUNT_SUFFIX);
        redisTemplate.opsForValue().increment(countKey, 1);
    }


    /**
     * Redis 키 패턴
     */
    private String getRedisKey(String prefix, String suffix) {
        return prefix + suffix;
    }

    /**
     * ChatMessage를 Json으로 변환합니다.
     *
     * @param chatMessage
     * @return json String
     */
    private String convertToJson(ChatMessage chatMessage) {
        try {
            return objectMapper.writeValueAsString(chatMessage);
        } catch (JsonProcessingException e) {
            throw new BaseException(CHATMESSAGE_NOT_FOUND);
//            log.error("Redis 캐시에 메시지 저장 실패: chatRoomId={}, error={}", chatMessage.getChatRoomId(), e.getMessage());
        }
    }

    /**
     * Json을 ChatMessage으로 변환합니다.
     *
     * @param json
     * @return ChatMessage
     */
    private ChatMessage convertFromJson(String json) {
        try {
            return objectMapper.readValue(json, ChatMessage.class);
        } catch (JsonProcessingException e) {
            throw new BaseException(CHATMESSAGE_NOT_FOUND);
        }
    }


}

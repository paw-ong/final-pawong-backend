package kr.co.pawong.pwbe.chat.adapter.out.cache.redis;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.CHATMESSAGE_NOT_FOUND;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
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


    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Redis에 채팅 메시지를 저장하고, 최대 n개만 보관되도록 trim 처리한다.
     * @param chatRoomId  캐시할 채팅방 ID
     * @param chatMessage 저장할 ChatMessageDto 객체
     * @param maxSize     최대 보관할 메시지 개수 (N)
     */
    @Override
    public void cacheMessage(Long chatRoomId, ChatMessage chatMessage, int maxSize) {
        String key = getRedisKey(chatRoomId);
        String json = convertToJson(chatMessage);

        redisTemplate.opsForList().rightPush(key, json);
        // 리스트 길이를 최근 maxSize건만 남기도록 조정
        // trim(start, end): 인덱스 기준. -maxSize ~ -1 이면 뒤에서 maxSize건
        redisTemplate.opsForList().trim(key, -maxSize, -1);
    }

    /**
     * Redis에서 해당 채팅룸의 최신 N건 메시지를 조회하여 DTO 리스트로 반환한다.
     *
     * @param chatRoomId  조회할 채팅방 ID
     * @param maxSize     가져올 최신 메시지 개수
     * @return 최신 N건의 ChatMessage 리스트 (없으면 빈 리스트)
     */
    public List<ChatMessage> getLatestMessages(Long chatRoomId, int maxSize) {
        String key = getRedisKey(chatRoomId);
        List<String> messageList = redisTemplate.opsForList().range(key, -maxSize, -1);

        if (messageList == null || messageList.isEmpty()) {
            return new ArrayList<>();
        }

        return messageList.stream()
                .map(this::convertFromJson)
                .toList();
    }

    /**
     * 채팅룸별 Redis 키 패턴
     */
    private String getRedisKey(Long chatRoomId) {
        return "chat:room:" + chatRoomId + ":messages";
    }

    /**
     * ChatMessage를 Json으로 변환합니다.
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

package kr.co.pawong.pwbe.global.util;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.REDIS_KEY_NOT_FOUND;

import java.time.Duration;
import kr.co.pawong.pwbe.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisUtils {
    private final StringRedisTemplate template;

    public String getData(String key) {
        ValueOperations<String, String> valueOperations = template.opsForValue();
        if (valueOperations.get(key) == null) {
            throw new BaseException(REDIS_KEY_NOT_FOUND);
        }
        return valueOperations.get(key);
    }

    public boolean existData(String key) {
        return Boolean.TRUE.equals(template.hasKey(key));
    }

    public void setDataExpire(String key, String value, Duration duration) {
        ValueOperations<String, String> valueOperations = template.opsForValue();
        valueOperations.set(key, value, duration);
    }

    public void deleteData(String key) {
        template.delete(key);
    }
}

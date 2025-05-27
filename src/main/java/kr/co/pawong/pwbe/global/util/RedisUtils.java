package kr.co.pawong.pwbe.global.util;

import java.time.Duration;
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
            return "false";
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

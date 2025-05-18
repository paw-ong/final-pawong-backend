package kr.co.pawong.pwbe.infrastructure.s3.util;

import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class StorageUtil {

    /**
     * Object Key 생성
     */
    public String createObjectKey(String directoryName, Long userId, String fileExtension) {
        // 키 생성 로직: userId + UUID + 확장자
        return String.format("%s/%d/%s%s",
                directoryName,
                userId,
                UUID.randomUUID(),
                fileExtension);
    }

}

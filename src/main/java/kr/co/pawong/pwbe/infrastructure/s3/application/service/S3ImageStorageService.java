package kr.co.pawong.pwbe.infrastructure.s3.application.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;
import kr.co.pawong.pwbe.infrastructure.s3.adapter.in.dto.response.PresignUrlResponse;
import kr.co.pawong.pwbe.infrastructure.s3.application.port.in.ImageStorageUseCase;
import kr.co.pawong.pwbe.infrastructure.s3.application.port.out.ImageStoragePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3ImageStorageService implements ImageStorageUseCase {

    private final ImageStoragePort storagePort;


    /**
     * S3로부터 이미지를 저장할 수 있는 URL를 받아옵니다
     *
     * @param objectKey
     * @param contentType
     * @param expires
     * @return
     */
    @Override
    public PresignUrlResponse presignUpload(String objectKey, String contentType, Duration expires) {
        PresignedPutObjectRequest presigned = storagePort.getPresignedPutObjectRequest(expires, objectKey, contentType);
        return new PresignUrlResponse(
                presigned.url().toString(),
                getStringStringMap(presigned),
                objectKey,
                Instant.now().plus(expires)     // 만료 시간 객체 생성
        );
    }


    /**
     * Map<String, List<String>> → Map<String, String> 변환 (첫 번째 값만 사용)
     *
     * @param presigned
     * @return
     */
    private static Map<String, String> getStringStringMap(PresignedPutObjectRequest presigned) {
        return presigned
                .signedHeaders()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().getFirst()
                ));
    }

}

package kr.co.pawong.pwbe.infrastructure.s3.adapter.out;

import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import kr.co.pawong.pwbe.infrastructure.s3.adapter.in.dto.response.PresignUrlResponse;
import kr.co.pawong.pwbe.infrastructure.s3.application.port.out.S3StoragePort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3StorageAdapter implements S3StoragePort {

    private final S3Presigner presigner;
    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    /**
     * S3로부터 이미지를 저장할 수 있는 URL를 받아옵니다
     *
     * @param key
     * @param contentType
     * @param expires
     * @return
     */
    @Override
    public PresignUrlResponse presignUpload(String key, String contentType, Duration expires) {
        PresignedPutObjectRequest presigned = getPresignedPutObjectRequest(expires,
                getPutObjectRequest(key, contentType));
        return new PresignUrlResponse(
                presigned.url().toString(),
                getStringStringMap(presigned),
                key,
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

    /**
     * S3 업로드용 presigned URL과, 요청에 필요한 서명 헤더들이 포함된 PresignedPutObjectRequest를 받습니다
     *
     * @param expires
     * @param putObjectRequest
     * @return
     */
    private PresignedPutObjectRequest getPresignedPutObjectRequest(Duration expires,
            PutObjectRequest putObjectRequest) {
        return presigner
                .presignPutObject(r -> r
                        .signatureDuration(expires)
                        .putObjectRequest(putObjectRequest)
                );
    }

    /**
     * S3에 업로드할 객체의 버킷, 키, 그리고 MIME 타입 조건을 담은 요청 객체를 만듭니다
     *
     * @param key
     * @param contentType
     * @return
     */
    private PutObjectRequest getPutObjectRequest(String key, String contentType) {
        return PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .build();
    }

    /**
     * ObjectKey로부터 presigned URL을 추출합니다.
     *
     * @param objectKey
     * @param expires
     * @return
     */
    @Override
    public URL presignDownload(String objectKey, Duration expires) {
        return presigner
                .presignGetObject(r -> r
                        .signatureDuration(expires)
                        .getObjectRequest(b -> b
                                .bucket(bucketName)
                                .key(objectKey))
                ).url();
    }

}

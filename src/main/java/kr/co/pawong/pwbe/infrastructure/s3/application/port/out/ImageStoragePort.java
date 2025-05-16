package kr.co.pawong.pwbe.infrastructure.s3.application.port.out;

import java.net.URL;
import java.time.Duration;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

public interface ImageStoragePort {

    PresignedPutObjectRequest getPresignedPutObjectRequest(Duration expires, String objectKey,
            String contentType);

    /**
     * 다운로드용(조회용) presigned URL 생성
     * @param objectKey
     * @param expires
     * @return
     */
    URL presignDownload(String objectKey, Duration expires);
}
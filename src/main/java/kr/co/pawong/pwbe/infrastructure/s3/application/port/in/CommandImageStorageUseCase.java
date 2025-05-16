package kr.co.pawong.pwbe.infrastructure.s3.application.port.in;

import java.time.Duration;
import kr.co.pawong.pwbe.infrastructure.s3.adapter.in.dto.response.PresignUrlResponse;

public interface CommandImageStorageUseCase {

    /**
     * 업로드용 presigned URL 생성
     *
     * @return presign URL + object key
     */
    PresignUrlResponse presignUpload(String objectKey, String contentType, Duration expires);
}
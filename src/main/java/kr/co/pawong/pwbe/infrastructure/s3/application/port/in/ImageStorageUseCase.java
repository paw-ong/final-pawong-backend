package kr.co.pawong.pwbe.infrastructure.s3.application.port.in;

import java.net.URL;
import java.time.Duration;
import kr.co.pawong.pwbe.infrastructure.s3.adapter.in.dto.response.PresignUrlResponse;

public interface ImageStorageUseCase {

    /**
     * 업로드용 presigned URL을 담은 Response 객체 생성
     * @param objectKey
     * @param contentType
     * @param expires
     * @return presign URL + object key
     */
    PresignUrlResponse presignUpload(String objectKey, String contentType, Duration expires);



}
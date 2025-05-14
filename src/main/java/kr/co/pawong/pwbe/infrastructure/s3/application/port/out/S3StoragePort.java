package kr.co.pawong.pwbe.infrastructure.s3.application.port.out;

import java.net.URL;
import java.time.Duration;
import kr.co.pawong.pwbe.infrastructure.s3.adapter.in.dto.response.PresignUrlResponse;

public interface S3StoragePort {

    /**
     * 업로드용 presigned URL 생성
     *
     * @return presign URL + object key
     */
    PresignUrlResponse presignUpload(String key, String contentType, Duration expires);

    /**
     * 다운로드용 presigned URL 생성
     */
    URL presignDownload(String key, Duration expires);

}

package kr.co.pawong.pwbe.infrastructure.s3.application.port.out;

import java.net.URL;
import java.time.Duration;
import kr.co.pawong.pwbe.infrastructure.s3.adapter.in.dto.response.PresignUrlResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

public interface S3StoragePort {


    PresignedPutObjectRequest getPresignedPutObjectRequest(Duration expires, String objectKey,
            String contentType);

    URL presignDownload(String objectKey, Duration expires);

}
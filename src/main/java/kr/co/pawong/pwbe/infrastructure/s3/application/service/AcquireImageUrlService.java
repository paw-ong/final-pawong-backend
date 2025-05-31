package kr.co.pawong.pwbe.infrastructure.s3.application.service;

import java.net.URL;
import java.time.Duration;
import kr.co.pawong.pwbe.infrastructure.s3.application.port.in.AcquireImageUrlUseCase;
import kr.co.pawong.pwbe.infrastructure.s3.application.port.out.ImageStoragePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AcquireImageUrlService implements AcquireImageUrlUseCase {

    private final ImageStoragePort imageStoragePort;

    private static final Duration DOWNLOAD_URL_EXPIRE = Duration.ofMinutes(15);

    @Override
    public URL acquireImageUrl(String objectKey) {
        return imageStoragePort.presignDownload(objectKey, DOWNLOAD_URL_EXPIRE);
    }
}

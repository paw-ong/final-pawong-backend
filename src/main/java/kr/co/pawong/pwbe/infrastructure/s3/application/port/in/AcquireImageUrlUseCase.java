package kr.co.pawong.pwbe.infrastructure.s3.application.port.in;

import java.net.URL;

public interface AcquireImageUrlUseCase {

    URL acquireImageUrl(String objectKey);
}

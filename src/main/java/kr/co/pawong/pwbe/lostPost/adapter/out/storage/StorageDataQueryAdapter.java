package kr.co.pawong.pwbe.lostPost.adapter.out.storage;

import kr.co.pawong.pwbe.infrastructure.s3.application.port.in.AcquireImageUrlUseCase;
import kr.co.pawong.pwbe.lostPost.application.port.out.StorageDataQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StorageDataQueryAdapter implements StorageDataQueryPort {

    private final AcquireImageUrlUseCase acquireImageUrlUseCase;

    @Override
    public String acquireImageUrl(String objectKey) {
        return acquireImageUrlUseCase.acquireImageUrl(objectKey).toString();
    }
}

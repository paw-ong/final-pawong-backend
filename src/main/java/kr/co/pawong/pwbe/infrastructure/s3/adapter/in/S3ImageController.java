package kr.co.pawong.pwbe.infrastructure.s3.adapter.in;

import java.time.Duration;
import kr.co.pawong.pwbe.infrastructure.s3.adapter.in.dto.request.PresignUrlRequest;
import kr.co.pawong.pwbe.infrastructure.s3.adapter.in.dto.response.PresignUrlResponse;
import kr.co.pawong.pwbe.infrastructure.s3.application.port.in.ImageStorageUseCase;
import kr.co.pawong.pwbe.infrastructure.s3.util.StorageUtil;
import kr.co.pawong.pwbe.user.adapter.in.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class S3ImageController {

    private final ImageStorageUseCase imageStorageUseCase;
    private final StorageUtil storageUtilService;

    @PostMapping("/presign-upload")
    public PresignUrlResponse presignUpload(
            @RequestBody PresignUrlRequest req,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return imageStorageUseCase.presignUpload(
                storageUtilService.createObjectKey(req.directoryName(), user.getUserId(), req.fileExtension()),
                req.contentType(),
                Duration.ofMinutes(req.expiresInMinutes())
        );
    }
}

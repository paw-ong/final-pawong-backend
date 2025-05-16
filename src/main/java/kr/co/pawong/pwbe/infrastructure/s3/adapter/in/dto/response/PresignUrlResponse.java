package kr.co.pawong.pwbe.infrastructure.s3.adapter.in.dto.response;

import java.time.Instant;
import java.util.Map;

public record PresignUrlResponse(
        /**
         * S3로 직접 PUT/GET 할 수 있는 presigned URL
         */
        String url,

        /**
         * presign 요청에 포함된 서명용 헤더들.
         * fetch/Axios 등에 꼭 함께 넘겨야 하는 x-amz-* 헤더들이 들어있습니다.
         */
        Map<String, String> headers,

        /**
         * S3에 저장될 object key.
         * 업로드 후 게시글 API 호출 시 이 값을 DB에 같이 저장합니다.
         */
        String objectKey,

        /**
         * (선택) presigned URL 만료 시각. Instant 또는 ISO-8601 문자열.
         * 클라이언트에서 남은 유효기간 표시 등에 활용할 수 있습니다.
         */
        Instant expiresAt
) {

}

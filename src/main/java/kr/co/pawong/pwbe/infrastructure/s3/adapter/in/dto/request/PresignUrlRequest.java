package kr.co.pawong.pwbe.infrastructure.s3.adapter.in.dto.request;


public record PresignUrlRequest(
        /**
         * 클라이언트에서 올리고자 하는 원본 파일명.
         * 서버에서 S3 object key 생성 시 확장자 추출 등에 사용합니다.
         */
        String fileName,
        /**
         * 파일의 MIME 타입. (e.g. "image/png", "image/jpeg")
         * presign 시 Content-Type 검증 조건으로 들어갑니다.
         */
        String contentType,
        /**
         * (선택) presign URL 유효기간(분).
         * 지정하지 않으면 서버 기본값(예: 10분)을 사용합니다.
         */
        Integer expiresInMinutes,
        /**
         * 파일의 화장자명
         * (e.g. ".png", "jpeg")
         */
        String fileExtension,
        /**
         * 파일이 저장될 디렉토리 명
         */
        String directoryName
) {

}

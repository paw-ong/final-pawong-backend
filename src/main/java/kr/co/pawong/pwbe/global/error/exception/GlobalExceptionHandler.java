package kr.co.pawong.pwbe.global.error.exception;

import kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode;
import kr.co.pawong.pwbe.global.error.errorcode.ErrorCode;
import kr.co.pawong.pwbe.global.error.response.BaseErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Object> handleBaseException(final BaseException exception) {
        final ErrorCode errorCode = exception.getErrorCode();
        log.error("BaseException : {}, errorCode : {}", exception, errorCode, exception);
        return handleExceptionInternal(errorCode, exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(final Exception exception) {
        log.error("Exception : {}", exception, exception);
        return handleExceptionInternal(CustomErrorCode.SERVER_ERROR, exception.getMessage());
    }

    private ResponseEntity<Object> handleExceptionInternal(final ErrorCode errorCode, final String message) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode, message));
    }

    private BaseErrorResponse makeErrorResponse(final ErrorCode errorCode, final String message) {
        return BaseErrorResponse.builder()
                .code(errorCode.name())
                .message(message)
                .build();
    }
}

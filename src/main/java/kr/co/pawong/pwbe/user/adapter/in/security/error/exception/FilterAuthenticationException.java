package kr.co.pawong.pwbe.user.adapter.in.security.error.exception;

import kr.co.pawong.pwbe.global.error.errorcode.ErrorCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class FilterAuthenticationException extends AuthenticationException {

    private final ErrorCode errorCode;

    public FilterAuthenticationException(ErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode;
    }

    public FilterAuthenticationException(ErrorCode errorCode, String detailMessage) {
        super(detailMessage);
        this.errorCode = errorCode;
    }
}

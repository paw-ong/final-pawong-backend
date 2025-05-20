package kr.co.pawong.pwbe.user.adapter.in.security.error;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.REQUEST_ERROR;
import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.USER_NOT_FOUND;

import java.util.Map;
import kr.co.pawong.pwbe.global.error.errorcode.ErrorCode;
import kr.co.pawong.pwbe.user.adapter.in.security.error.exception.FilterAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationExceptionTranslator {

    private final Map<Class<? extends AuthenticationException>, ErrorCode> map = Map.of(
            FilterAuthenticationException.class, USER_NOT_FOUND
    );

    public ErrorCode translate(AuthenticationException ex) {
        if (ex instanceof FilterAuthenticationException e) {
            return e.getErrorCode();
        }
        return map.getOrDefault(ex.getClass(), REQUEST_ERROR);
    }
}

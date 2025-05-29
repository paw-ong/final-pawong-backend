package kr.co.pawong.pwbe.global.security.error;

import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.ACCESS_TOKEN_INVALIDATE;
import static kr.co.pawong.pwbe.global.error.errorcode.CustomErrorCode.TOKEN_INVALIDATE;

import java.util.Map;
import kr.co.pawong.pwbe.global.error.errorcode.ErrorCode;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationExceptionTranslator {

    private Map<Class<? extends AuthenticationException>, ErrorCode> map = Map.of();

    public ErrorCode translate(AuthenticationException ex) {
        if(ex instanceof InsufficientAuthenticationException)
            map.put(ex.getClass(), TOKEN_INVALIDATE);

        return map.getOrDefault(ex.getClass(), ACCESS_TOKEN_INVALIDATE);
    }
}

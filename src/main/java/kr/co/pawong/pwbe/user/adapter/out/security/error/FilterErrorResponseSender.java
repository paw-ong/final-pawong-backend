package kr.co.pawong.pwbe.user.adapter.out.security.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import kr.co.pawong.pwbe.global.error.errorcode.ErrorCode;
import kr.co.pawong.pwbe.global.error.response.BaseErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class FilterErrorResponseSender {

    private final ObjectMapper mapper;

    public FilterErrorResponseSender(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public void send(HttpServletResponse response, ErrorCode code) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(code.getHttpStatus().value());

        BaseErrorResponse body = BaseErrorResponse.builder()
                .code(code.name())
                .message(code.getMessage())
                .build();

        mapper.writeValue(response.getWriter(), body);
    }
}

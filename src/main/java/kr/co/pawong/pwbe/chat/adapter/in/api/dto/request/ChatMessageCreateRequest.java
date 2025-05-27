package kr.co.pawong.pwbe.chat.adapter.in.api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageCreateRequest {

    @Setter
    private Long roomId;
    private String sender;
    private String content;

    /**
     * ISO-8601 포맷 문자열을 Instant 로 파싱하려면
     * application.yml 에서 jackson:
     *   serialization:
     *     write-dates-as-timestamps: false
     *   deserialization:
     *     accept-iso-8601-dates: true
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    private Instant timestamp;
}
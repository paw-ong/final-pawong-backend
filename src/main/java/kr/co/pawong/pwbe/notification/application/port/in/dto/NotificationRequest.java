package kr.co.pawong.pwbe.notification.application.port.in.dto;

import kr.co.pawong.pwbe.lostPost.enums.PostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    private Long userId;
    private String message;
    private Long targetId;
    private PostType postType;
}

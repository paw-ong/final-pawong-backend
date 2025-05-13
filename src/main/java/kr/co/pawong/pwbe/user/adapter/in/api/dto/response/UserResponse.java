package kr.co.pawong.pwbe.user.adapter.in.api.dto.response;

import java.time.LocalDateTime;
import kr.co.pawong.pwbe.user.domain.User;
import kr.co.pawong.pwbe.user.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long userId;
    private String nickname;
    private String profileImage;
    private String region;
    private String tel;
    private UserStatus status;
    private LocalDateTime createdAt;

    public UserResponse(User user) {
        this.userId = user.getUserId();
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();
        this.region = user.getRegion();
        this.tel = user.getTel();
        this.status = user.getStatus();
        this.createdAt = user.getCreatedAt();
    }

}

package kr.co.pawong.pwbe.user.domain;

import java.time.LocalDateTime;
import kr.co.pawong.pwbe.user.application.port.in.dto.UserCreate;
import kr.co.pawong.pwbe.user.application.port.in.dto.UserUpdate;
import kr.co.pawong.pwbe.user.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {
  private Long userId;
  private Long socialId;
  private String nickname;
  private String profileImage;
  private String region;
  private String tel;
  private UserStatus status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime deletedAt;
  private String email;

    public static User from(UserCreate userCreate) {
        return User.builder()
                .socialId(userCreate.getSocialId())
                .nickname(userCreate.getNickname())
                .profileImage(userCreate.getProfileImage())
                .status(UserStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
    }

  public User update(UserUpdate userUpdate) {
    this.nickname = userUpdate.getNickname();
    this.region = userUpdate.getRegion();
    this.tel = userUpdate.getTel();
    this.email = userUpdate.getEmail();
    this.updatedAt = LocalDateTime.now();
    this.status = UserStatus.ACTIVE;
    return this;
  }
}